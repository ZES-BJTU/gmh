package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.GenderEnum;
import com.zes.squad.gmh.common.enums.UserRoleEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.UserQueryCondition;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.union.UserUnion;
import com.zes.squad.gmh.service.MessageService;
import com.zes.squad.gmh.service.UserService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.MessageParams;
import com.zes.squad.gmh.web.entity.param.UserChangePasswordParams;
import com.zes.squad.gmh.web.entity.param.UserCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.UserLoginParams;
import com.zes.squad.gmh.web.entity.param.UserQueryParams;
import com.zes.squad.gmh.web.entity.vo.UserVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/users")
@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService    userService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/login", method = { RequestMethod.POST })
    public JsonResult<UserVo> doLoginWithAccount(@RequestBody UserLoginParams params) {
        ensureParameterExist(params, "登录信息为空");
        ensureParameterExist(params.getAccount(), "账号为空");
        ensureParameterExist(params.getPassword(), "密码为空");
        UserUnion union = userService.loginWithAccount(params.getAccount(), params.getPassword());
        UserVo vo = buildUserVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/changePassword", method = { RequestMethod.PATCH })
    public JsonResult<Void> doChangePassword(@RequestBody UserChangePasswordParams params) {
        ensureParameterExist(params, "原密码为空");
        ensureParameterExist(params.getOriginalPassword(), "原密码为空");
        ensureParameterExist(params.getNewPassword(), "新密码为空");
        ensureParameterValid(!Objects.equals(params.getOriginalPassword(), params.getNewPassword()), "原密码不能和新密码相同");
        UserUnion union = getUser();
        userService.changePassword(union.getId(), params.getOriginalPassword(), params.getNewPassword());
        return JsonResults.success();
    }

    @RequestMapping(path = "/resetPassword", method = { RequestMethod.PATCH })
    public JsonResult<Void> doResetPassword(@RequestBody MessageParams params) {
        ensureParameterExist(params, "短信参数为空");
        ensureParameterExist(params.getMobile(), "手机号为空");
        ensureParameterExist(params.getAuthCode(), "验证码为空");
        messageService.validateAuthCode(params.getMobile(), params.getAuthCode());
        userService.resetPassword(params.getMobile());
        return JsonResults.success();
    }

    @RequestMapping(path = "/detail", method = { RequestMethod.GET })
    public JsonResult<UserVo> doQueryUserDetail() {
        UserUnion union = getUser();
        UserVo vo = buildUserVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/logout", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doLogout() {
        UserUnion user = getUser();
        if (user == null) {
            return JsonResults.success();
        }
        userService.logout(user.getId());
        unBind();
        return JsonResults.success();
    }

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<UserVo> doCreateUser(@RequestBody UserCreateOrModifyParams params) {
        checkUserCreateParams(params);
        UserPo po = CommonConverter.map(params, UserPo.class);
        UserPo newUserPo = userService.createUser(po);
        UserVo vo = buildUserVoByPo(newUserPo);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveUser(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除用户");
        userService.removeUser(id);
        return JsonResults.success();
    }

    @RequestMapping(method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveUsers(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除用户");
        userService.removeUsers(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<UserVo> doModifyUser(@PathVariable Long id, @RequestBody UserCreateOrModifyParams params) {
        checkUserModifyParams(id, params);
        UserPo po = CommonConverter.map(params, UserPo.class);
        UserPo newUserPo = userService.modifyUser(po);
        UserVo vo = buildUserVoByPo(newUserPo);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<UserVo>> doListPagedUsers(UserQueryParams queryParams) {
        ensureParameterExist(queryParams, "用户查询条件为空");
        CheckHelper.checkPageParams(queryParams);
        if (queryParams.getRole() != null) {
            ensureParameterValid(EnumUtils.containsKey(queryParams.getRole(), UserRoleEnum.class), "用户角色错误");
        }
        UserQueryCondition condition = CommonConverter.map(queryParams, UserQueryCondition.class);
        PagedList<UserUnion> pagedUserUnions = userService.listPagedUsers(condition);
        if (pagedUserUnions == null || CollectionUtils.isEmpty(pagedUserUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(queryParams.getPageNum(), queryParams.getPageSize()));
        }
        List<UserVo> vos = Lists.newArrayListWithCapacity(pagedUserUnions.getData().size());
        for (UserUnion union : pagedUserUnions.getData()) {
            UserVo vo = buildUserVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUserUnions.getPageNum(), pagedUserUnions.getPageSize(),
                pagedUserUnions.getTotalCount(), vos));
    }

    private UserVo buildUserVoByPo(UserPo po) {
        UserVo vo = CommonConverter.map(po, UserVo.class);
        vo.setRole(EnumUtils.getDescByKey(po.getRole().intValue(), UserRoleEnum.class));
        vo.setGender(EnumUtils.getDescByKey(po.getGender().intValue(), GenderEnum.class));
        return vo;
    }

    private void checkUserCreateParams(UserCreateOrModifyParams params) {
        UserPo user = ThreadContext.getCurrentUser();
        ensureParameterExist(params, "用户信息为空");
        ensureParameterNotExist(params.getId(), "用户标识应为空");
        ensureParameterExist(params.getRole(), "用户身份为空");
        ensureParameterValid(EnumUtils.containsKey(params.getRole(), UserRoleEnum.class), "用户身份错误");
        if (user.getRole().intValue() == UserRoleEnum.ADMINISTRATOR.getKey()) {
            ensureParameterValid(params.getRole().intValue() == UserRoleEnum.MANAGER.getKey(), "管理员只可以新建门店负责人");
        } else if (user.getRole().intValue() == UserRoleEnum.MANAGER.getKey()) {
            ensureParameterValid(params.getRole().intValue() != UserRoleEnum.ADMINISTRATOR.getKey()
                    && params.getRole().intValue() != UserRoleEnum.MANAGER.getKey(), "门店负责人只可以新建前台和员工");
        }
        ensureParameterExist(params.getEmail(), "用户邮箱不能为空");
        params.setAccount(params.getEmail());
        ensureParameterValid(CheckHelper.isValidEmail(params.getEmail()), "用户邮箱格式错误");
        ensureParameterExist(params.getMobile(), "用户手机号不能为空");
        ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "用户手机号格式错误");
        ensureParameterExist(params.getName(), "用户姓名为空");
        ensureParameterExist(params.getGender(), "用户性别为空");
        ensureParameterValid(EnumUtils.containsKey(params.getGender().intValue(), GenderEnum.class), "用户性别错误");
        if (user.getRole().intValue() == UserRoleEnum.ADMINISTRATOR.getKey()) {
            ensureParameterExist(params.getStoreId(), "门店负责人所属门店为空");
        } else {
            params.setStoreId(ThreadContext.getUserStoreId());
        }
    }

    private void checkUserModifyParams(Long id, UserCreateOrModifyParams params) {
        ensureParameterExist(id, "用户信息为空");
        ensureParameterExist(params, "用户信息为空");
        params.setId(id);
        if (!Strings.isNullOrEmpty(params.getEmail())) {
            ensureParameterValid(CheckHelper.isValidEmail(params.getEmail()), "用户邮箱格式错误");
            params.setAccount(params.getEmail());
        }
        if (!Strings.isNullOrEmpty(params.getMobile())) {
            ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "用户手机号格式错误");
        }
        if (params.getGender() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getGender().intValue(), GenderEnum.class), "用户性别错误");
        }
        if (params.getRole() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getRole(), UserRoleEnum.class), "用户身份错误");
        }
        ensureParameterExist(params.getStoreId(), "用户所属门店为空");
        UserPo user = ThreadContext.getCurrentUser();
        if (user.getRole().intValue() == UserRoleEnum.ADMINISTRATOR.getKey()) {
            ensureParameterValid(params.getRole().intValue() == UserRoleEnum.MANAGER.getKey(), "管理员只可以修改门店负责人信息");
        } else if (user.getRole().intValue() == UserRoleEnum.MANAGER.getKey()) {
            ensureParameterValid(params.getStoreId().equals(user.getStoreId()), "门店负责人只可以修改当前门店人员信息");
        }
    }

}
