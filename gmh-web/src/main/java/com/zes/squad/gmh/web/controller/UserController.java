package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.UserGenderEnum;
import com.zes.squad.gmh.common.enums.UserRoleEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.UserQueryCondition;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.helper.CheckHelper;
import com.zes.squad.gmh.web.param.user.UserCreateOrModifyParams;
import com.zes.squad.gmh.web.param.user.UserDeleteParams;
import com.zes.squad.gmh.web.param.user.UserLoginParams;
import com.zes.squad.gmh.web.param.user.UserPasswordParams;
import com.zes.squad.gmh.web.param.user.UserQueryParams;
import com.zes.squad.gmh.web.vo.UserVo;

@RequestMapping(path = "/user")
@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/login", method = { RequestMethod.GET })
    public JsonResult<UserVo> doLoginWithAccount(@RequestBody UserLoginParams params) {
        ensureParameterExist(params, "登录信息为空");
        ensureParameterExist(params.getAccount(), "账号为空");
        ensureParameterExist(params.getPassword(), "密码为空");
        UserUnion union = userService.loginWithAccount(params.getAccount(), params.getPassword());
        UserVo vo = buildUserVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/changePassword", method = { RequestMethod.POST })
    public JsonResult<UserVo> doChangePassword(@RequestBody UserPasswordParams params) {
        ensureParameterExist(params, "原密码为空");
        ensureParameterExist(params.getOriginalPassword(), "原密码为空");
        ensureParameterExist(params.getNewPassword(), "新密码为空");
        ensureParameterValid(Objects.equals(params.getOriginalPassword(), params.getNewPassword()), "原密码不能和新密码相同");
        UserUnion union = getUser();
        userService.changePassword(union.getId(), params.getOriginalPassword(), params.getNewPassword());
        return JsonResults.success();
    }
    
    @RequestMapping(path = "/resetPassword", method = {RequestMethod.POST})
    public JsonResult<Void> soResetPassword() {
        return JsonResults.success();
    }

    @RequestMapping(path = "/detail", method = { RequestMethod.GET })
    public JsonResult<UserVo> doQueryUserDetail() {
        UserUnion union = getUser();
        UserVo vo = buildUserVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateUser(@RequestBody UserCreateOrModifyParams params) {
        checkUserCreateParams(params);
        UserPo po = CommonConverter.map(params, UserPo.class);
        userService.createUser(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/modify", method = { RequestMethod.POST })
    public JsonResult<Void> doModifyUser(@RequestBody UserCreateOrModifyParams params) {
        checkUserModifyParams(params);
        UserPo po = CommonConverter.map(params, UserPo.class);
        userService.modifyUser(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/listByPage", method = { RequestMethod.GET })
    public JsonResult<PagedList<UserVo>> doListPagedUsers(@RequestBody UserQueryParams params) {
        ensureParameterExist(params, "查询条件为空");
        ensureParameterExist(params.getPageNum(), "分页页码为空");
        ensureParameterValid(params.getPageNum() > 0, "分页页码错误");
        ensureParameterExist(params.getPageSize(), "分页大小为空");
        ensureParameterValid(params.getPageSize() > 0, "分页大小错误");
        UserQueryCondition condition = CommonConverter.map(params, UserQueryCondition.class);
        PagedList<UserUnion> pagedUserUnions = userService.listPagedUsers(condition);
        if (pagedUserUnions == null || CollectionUtils.isEmpty(pagedUserUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(params.getPageNum(), params.getPageSize()));
        }
        List<UserVo> vos = Lists.newArrayListWithCapacity(pagedUserUnions.getData().size());
        for (UserUnion union : pagedUserUnions.getData()) {
            UserVo vo = buildUserVoByUnion(union);
            vo.setToken(null);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUserUnions.getPageNum(), pagedUserUnions.getPageSize(),
                pagedUserUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/remove/{id}", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveUser(@PathVariable("id") Long id) {
        ensureParameterExist(id, "待删除用户标识为空");
        userService.removeById(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/batchRemove", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveUser(@RequestBody UserDeleteParams params) {
        ensureParameterExist(params, "待删除用户为空");
        ensureCollectionNotEmpty(params.getIds(), "待删除用户为空");
        userService.batchRemove(params.getIds());
        return JsonResults.success();
    }
    
    private UserVo buildUserVoByUnion(UserUnion union) {
        UserVo vo = CommonConverter.map(union.getUserPo(), UserVo.class);
        vo.setRole(EnumUtils.getDescByKey(union.getUserPo().getRole().intValue(), UserRoleEnum.class));
        vo.setGender(EnumUtils.getDescByKey(union.getUserPo().getGender().intValue(), UserGenderEnum.class));
        vo.setToken(union.getToken());
        vo.setStoreName(union.getStoreName());
        return vo;
    }

    private void checkUserCreateParams(UserCreateOrModifyParams params) {
        ensureParameterExist(params, "用户信息为空");
        ensureParameterNotExist(params.getId(), "用户标识应为空");
        ensureParameterExist(params.getRole(), "用户身份为空");
        ensureParameterValid(EnumUtils.containsKey(params.getRole(), UserRoleEnum.class), "用户身份错误");
        ensureParameterExist(params.getAccount(), "用户账号不能为空");
        ensureParameterExist(params.getEmail(), "用户邮箱不能为空");
        ensureParameterValid(CheckHelper.isValidEmail(params.getEmail()), "用户邮箱格式错误");
        ensureParameterExist(params.getMobile(), "用户手机号不能为空");
        ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "用户手机号格式错误");
        ensureParameterExist(params.getName(), "用户姓名为空");
        ensureParameterExist(params.getGender(), "用户性别为空");
        ensureParameterValid(EnumUtils.containsKey(params.getGender().intValue(), UserGenderEnum.class), "用户性别错误");
        ensureParameterExist(params.getStoreId(), "用户所属门店为空");
    }

    private void checkUserModifyParams(UserCreateOrModifyParams params) {
        ensureParameterExist(params, "用户信息为空");
        ensureParameterExist(params.getId(), "用户标识不能为空");
        if (!Strings.isNullOrEmpty(params.getEmail())) {
            ensureParameterValid(CheckHelper.isValidEmail(params.getEmail()), "用户邮箱格式错误");
        }
        if (!Strings.isNullOrEmpty(params.getMobile())) {
            ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "用户手机号格式错误");
        }
        if (params.getGender() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getGender().intValue(), UserGenderEnum.class), "用户性别错误");
        }
    }

}
