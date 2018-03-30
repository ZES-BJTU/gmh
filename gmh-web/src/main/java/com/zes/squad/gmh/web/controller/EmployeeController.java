package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.util.Date;
import java.util.List;

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
import com.zes.squad.gmh.common.enums.WorkTypeEnum;
import com.zes.squad.gmh.common.enums.WorkingEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeePo;
import com.zes.squad.gmh.entity.po.EmployeeWorkPo;
import com.zes.squad.gmh.entity.union.EmployeeUnion;
import com.zes.squad.gmh.service.EmployeeService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.EmployeeParams;
import com.zes.squad.gmh.web.entity.param.EmployeeWorkQueryParams;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeWorkVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/employees")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<EmployeeVo> doCreateEmployee(@RequestBody EmployeeParams params) {
        checkEmployeCreateParams(params);
        EmployeeUnion union = buildEmployeeUnionByParams(params);
        EmployeeUnion newUnion = employeeService.createEmployee(union);
        EmployeeVo vo = buildEmployeeVoByUnion(newUnion);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveEmployee(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择离职员工");
        employeeService.removeEmployee(id);
        return JsonResults.success();
    }

    @RequestMapping(method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveEmployees(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择离职员工");
        employeeService.removeEmployees(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<EmployeeVo> doModifyEmployee(@PathVariable("id") Long id, @RequestBody EmployeeParams params) {
        checkEmployeModifyParams(id, params);
        EmployeeUnion union = buildEmployeeUnionByParams(params);
        EmployeeUnion newUnion = employeeService.modifyEmployee(union);
        EmployeeVo vo = buildEmployeeVoByUnion(newUnion);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<EmployeeVo> doQueryEmployeeDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "员工标识为空");
        EmployeeUnion union = employeeService.queryEmployeeDetail(id);
        EmployeeVo vo = buildEmployeeVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<EmployeeVo>> doListPagedEmployees(EmployeeWorkQueryParams queryParams) {
        ensureParameterExist(queryParams, "员工查询条件为空");
        checkEmployeeQueryParams(queryParams);
        EmployeeWorkQueryCondition condition = CommonConverter.map(queryParams, EmployeeWorkQueryCondition.class);
        PagedList<EmployeeUnion> pagedUnions = employeeService.listPagedEmployees(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<EmployeeVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (EmployeeUnion union : pagedUnions.getData()) {
            EmployeeVo vo = buildEmployeeVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    private EmployeeUnion buildEmployeeUnionByParams(EmployeeParams params) {
        EmployeePo employeePo = CommonConverter.map(params, EmployeePo.class);
        List<EmployeeWorkPo> workPos = Lists.newArrayListWithCapacity(params.getWorkTypes().size());
        for (Integer workType : params.getWorkTypes()) {
            EmployeeWorkPo workPo = new EmployeeWorkPo();
            workPo.setWorkType(workType);
            workPos.add(workPo);
        }
        EmployeeUnion union = new EmployeeUnion();
        union.setEmployeePo(employeePo);
        union.setEmployeeWorkPos(workPos);
        return union;
    }

    private void checkEmployeCreateParams(EmployeeParams params) {
        ensureParameterExist(params, "员工信息为空");
        ensureParameterNotExist(params.getId(), "员工标识应为空");
        ensureParameterExist(params.getName(), "员工姓名为空");
        ensureParameterExist(params.getGender(), "员工性别为空");
        ensureParameterValid(EnumUtils.containsKey(params.getGender(), GenderEnum.class), "员工性别错误");
        ensureParameterExist(params.getMobile(), "员工手机号为空");
        ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "员工手机号格式错误");
        ensureCollectionNotEmpty(params.getWorkTypes(), "员工工种为空");
        for (Integer workType : params.getWorkTypes()) {
            ensureParameterExist(workType, "员工工种为空");
            ensureParameterValid(EnumUtils.containsKey(workType, WorkTypeEnum.class), "员工工种错误");
        }
    }

    private void checkEmployeModifyParams(Long id, EmployeeParams params) {
        ensureParameterExist(id, "请选择待员工");
        ensureParameterExist(params, "请选择员工");
        params.setId(id);
        if (params.getGender() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getGender(), GenderEnum.class), "员工性别错误");
        }
        if (!Strings.isNullOrEmpty(params.getMobile())) {
            ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "员工手机号格式错误");
        }
        if (params.getEntryTime() != null) {
            ensureParameterValid(params.getEntryTime().before(new Date()), "员工入职时间错误");
        }
        if (CollectionUtils.isNotEmpty(params.getWorkTypes())) {
            for (Integer workType : params.getWorkTypes()) {
                ensureParameterExist(workType, "员工工种为空");
                ensureParameterValid(EnumUtils.containsKey(workType, WorkTypeEnum.class), "员工工种错误");
            }
        }
    }

    private void checkEmployeeQueryParams(EmployeeWorkQueryParams params) {
        CheckHelper.checkPageParams(params);
        if (params.getStartEntryTime() != null && params.getEndEntryTime() != null) {
            ensureParameterValid(params.getStartEntryTime().before(params.getEndEntryTime()), "员工入职查询起止时间段错误");
        }
        if (params.getWorkType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getWorkType(), WorkTypeEnum.class), "员工工种分类错误");
        }
    }

    private EmployeeVo buildEmployeeVoByUnion(EmployeeUnion union) {
        EmployeeVo vo = CommonConverter.map(union.getEmployeePo(), EmployeeVo.class);
        vo.setGender(EnumUtils.getDescByKey(union.getEmployeePo().getGender(), GenderEnum.class));
        vo.setWorking(EnumUtils.getDescByKey(union.getEmployeePo().getWorking(), WorkingEnum.class));
        List<EmployeeWorkVo> workVos = Lists.newArrayListWithCapacity(union.getEmployeeWorkPos().size());
        for (EmployeeWorkPo workPo : union.getEmployeeWorkPos()) {
            EmployeeWorkVo workVo = CommonConverter.map(workPo, EmployeeWorkVo.class);
            workVo.setWorkTypeDesc(EnumUtils.getDescByKey(workPo.getWorkType(), WorkTypeEnum.class));
            workVos.add(workVo);
        }
        vo.setEmployeeWorkVos(workVos);
        return vo;
    }

}
