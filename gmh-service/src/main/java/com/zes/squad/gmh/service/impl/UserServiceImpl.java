package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.cache.CacheService;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EncryptUtils;
import com.zes.squad.gmh.common.util.JsonUtils;
import com.zes.squad.gmh.constant.CacheConsts;
import com.zes.squad.gmh.entity.condition.UserQueryCondition;
import com.zes.squad.gmh.entity.po.StorePo;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.po.UserTokenPo;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.helper.SMSHelper;
import com.zes.squad.gmh.mapper.StoreMapper;
import com.zes.squad.gmh.mapper.UserMapper;
import com.zes.squad.gmh.mapper.UserTokenMapper;
import com.zes.squad.gmh.mapper.UserUnionMapper;
import com.zes.squad.gmh.property.MessageProperties;
import com.zes.squad.gmh.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private UserMapper          userMapper;
    @Autowired
    private UserTokenMapper     userTokenMapper;
    @Autowired
    private CacheService        cacheService;
    @Autowired
    private StoreMapper         storeMapper;
    @Autowired
    private UserUnionMapper     userUnionMapper;

    @Override
    public UserUnion loginWithAccount(String account, String password) {
        UserPo po = userMapper.selectByAccount(account);
        ensureEntityExist(po, "用户不存在");
        String encryptPassword = encryptPassword(po.getAccount(), po.getSalt(), password);
        if (!Objects.equals(po.getPassword(), encryptPassword)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "密码错误");
        }
        boolean isCacheValid = cacheService.isValid();
        if (isCacheValid) {
            UserTokenPo userTokenPo = userTokenMapper.selectByUserId(po.getId());
            String cacheKey = String.format(CacheConsts.CACHE_KEY_USER_PREFIX, userTokenPo.getToken());
            cacheService.delete(cacheKey);
        }
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        UserTokenPo tokenPo = new UserTokenPo();
        tokenPo.setUserId(po.getId());
        tokenPo.setToken(token);
        tokenPo.setLoginTime(new Date());
        userTokenMapper.insertOrUpdate(tokenPo);
        UserUnion userUnion = new UserUnion();
        userUnion.setId(po.getId());
        userUnion.setUserPo(po);
        userUnion.setUserTokenPo(tokenPo);
        return userUnion;
    }

    @Override
    public void changePassword(Long id, String originalPassword, String newPassword) {
        UserPo po = userMapper.selectById(id);
        String originalEncryptPassword = encryptPassword(po.getAccount(), po.getSalt(), originalPassword);
        ensureParameterValid(Objects.equals(originalEncryptPassword, po.getPassword()), "原密码错误");
        String newEncryptPassword = encryptPassword(po.getAccount(), po.getSalt(), newPassword);
        userMapper.updatePassword(newEncryptPassword);
    }

    @Override
    public void resetPassword(String mobile) {
        UserPo po = userMapper.selectByMobile(mobile);
        String resetPassword = encryptPassword(po.getAccount(), po.getSalt(), DEFAULT_PASSWORD);
        String resetPasswordContent = MessageProperties.get("reset.password.content");
        SMSHelper.sendMessage(po.getMobile(), MessageFormat.format(resetPasswordContent, DEFAULT_PASSWORD));
        userMapper.updatePassword(resetPassword);
    }

    @Override
    public UserUnion queryUserByToken(String token) {
        boolean isCacheValid = cacheService.isValid();
        UserUnion user = null;
        if (!isCacheValid) {
            return queryUserByTokenFromDb(token);
        }
        String cacheKey = String.format(CacheConsts.CACHE_KEY_USER_PREFIX, token);
        user = cacheService.get(cacheKey, UserUnion.class);
        if (user != null) {
            return user;
        }
        user = queryUserByTokenFromDb(token);
        if (user != null) {
            cacheService.put(cacheKey, user, CacheConsts.THIRTY_MINUTES);
        }
        return user;
    }

    @Override
    public void logout(Long userId) {
        UserTokenPo tokenPo = userTokenMapper.selectByUserId(userId);
        ensureEntityExist(tokenPo, "未找到用户");
        boolean isCacheValid = cacheService.isValid();
        if (isCacheValid) {
            //删除缓存中用户信息
            String token = tokenPo.getToken();
            String cacheKey = String.format(CacheConsts.CACHE_KEY_USER_PREFIX, token);
            cacheService.delete(cacheKey);
        }
        userTokenMapper.deleteByUserId(userId);
    }

    @Override
    public UserPo createUser(UserPo po) {
        StorePo storePo = storeMapper.selectById(po.getStoreId());
        ensureEntityExist(storePo, "门店错误");
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        String password = encryptPassword(po.getAccount(), salt, DEFAULT_PASSWORD);
        po.setSalt(salt);
        po.setPassword(password);
        userMapper.insert(po);
        return po;
    }

    @Override
    public void removeUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void removeUsers(List<Long> ids) {
        userMapper.batchDelete(ids);
    }

    @Override
    public UserPo modifyUser(UserPo po) {
        if (po.getStoreId() != null) {
            StorePo storePo = storeMapper.selectById(po.getStoreId());
            ensureEntityExist(storePo, "门店错误");
        }
        userMapper.updateSelective(po);
        UserPo newUserPo = userMapper.selectById(po.getId());
        ensureEntityExist(newUserPo, "用户信息不存在");
        return newUserPo;
    }

    @Override
    public PagedList<UserUnion> listPagedUsers(UserQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Long> ids = userMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            log.warn("用户信息集合为空, condition is {}", JsonUtils.toJson(condition));
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        List<UserUnion> unions = userUnionMapper.selectUserUnionsByIds(ids);
        PageInfo<Long> pageInfo = new PageInfo<>(ids);
        return PagedLists.newPagedList(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), unions);
    }

    @Override
    public List<UserUnion> listUsersByRole(Integer role) {
        return userUnionMapper.selectUserUnionsByRole(role);
    }

    private UserUnion queryUserByTokenFromDb(String token) {
        UserTokenPo tokenPo = userTokenMapper.selectByToken(token);
        if (tokenPo == null) {
            log.error("根据token查询用户失败, token is {}", token);
            return null;
        }
        UserPo userPo = userMapper.selectById(tokenPo.getUserId());
        if (userPo == null) {
            log.error("根据用户id查询用户失败, token is {}, user id is {}", token, tokenPo.getUserId());
            return null;
        }
        StorePo storePo = storeMapper.selectById(userPo.getStoreId());
        if (storePo == null) {
            log.error("根据门店id查询门店失败, token is {}, user id is {}, store id is {}", token,
                    tokenPo.getUserId(), userPo.getStoreId());
        }
        UserUnion user = new UserUnion();
        user.setId(userPo.getId());
        user.setUserPo(userPo);
        user.setUserTokenPo(tokenPo);
        user.setStorePo(storePo);
        return user;
    }

    private String encryptPassword(String account, String salt, String password) {
        return EncryptUtils.md5(account + salt + password);
    }

}
