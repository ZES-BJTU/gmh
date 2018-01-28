package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.util.Date;
import java.util.List;

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
import com.zes.squad.gmh.common.enums.TopTypeEnums;
import com.zes.squad.gmh.common.enums.GenderEnum;
import com.zes.squad.gmh.common.enums.WorkingEnums;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
import com.zes.squad.gmh.entity.condition.EmployeeWorkTypeQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeePo;
import com.zes.squad.gmh.entity.po.EmployeeWorkPo;
import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;
import com.zes.squad.gmh.entity.union.EmployeeUnion;
import com.zes.squad.gmh.entity.union.EmployeeWorkTypeUnion;
import com.zes.squad.gmh.entity.union.EmployeeWorkUnion;
import com.zes.squad.gmh.service.EmployeeService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.employee.EmployeeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.employee.EmployeeWorkQueryParams;
import com.zes.squad.gmh.web.entity.param.employee.EmployeeWorkTypeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.employee.EmployeeWorkTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.EmployeeVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeWorkTypeVo;
import com.zes.squad.gmh.web.entity.vo.EmployeeWorkVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/employee")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(path = "/workType/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateEmployeeWorkType(@RequestBody EmployeeWorkTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "员工工种信息为空");
        ensureParameterNotExist(params.getId(), "员工工种标识应为空");
        ensureParameterExist(params.getTopType(), "员工工种顶层分类为空");
        ensureParameterExist(params.getName(), "员工工种名称为空");
        ensureParameterValid(EnumUtils.containsKey(params.getTopType().intValue(), TopTypeEnums.class), "员工工种顶层分类错误");
        EmployeeWorkTypePo po = CommonConverter.map(params, EmployeeWorkTypePo.class);
        employeeService.createEmployeeWorkType(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/workType/{id}", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveEmployeeWorkType(@PathVariable("id") Long id) {
        ensureParameterExist(id, "员工工种标识为空");
        employeeService.removeEmployeeWrokType(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/workType/remove", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveEmployeeWorkTypes(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "员工工种标识为空");
        employeeService.removeEmployeeWorkTypes(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/workType/modify", method = { RequestMethod.POST })
    public JsonResult<Void> doModifyEmployeeWorkType(@RequestBody EmployeeWorkTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "员工工种信息为空");
        ensureParameterExist(params.getId(), "员工工种标识为空");
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType().intValue(), TopTypeEnums.class),
                    "员工工种顶层分类错误");
        }
        EmployeeWorkTypePo po = CommonConverter.map(params, EmployeeWorkTypePo.class);
        employeeService.modifyEmployeeWorkType(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/workType/{id}", method = { RequestMethod.GET })
    public JsonResult<EmployeeWorkTypeVo> doQueryEmployeeWorkTypeDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "员工工种标识为空");
        EmployeeWorkTypeUnion union = employeeService.queryEmployeeWorkTypeDetail(id);
        EmployeeWorkTypeVo vo = buildEmployeeWorkTypeVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/workType/list", method = { RequestMethod.GET })
    public JsonResult<PagedList<EmployeeWorkTypeVo>> doListPagedEmployeeWorkTypes(@RequestBody EmployeeWorkTypeQueryParams params) {
        CheckHelper.checkPageParams(params);
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType().intValue(), TopTypeEnums.class),
                    "员工工种顶层分类错误");
        }
        EmployeeWorkTypeQueryCondition condition = CommonConverter.map(params, EmployeeWorkTypeQueryCondition.class);
        PagedList<EmployeeWorkTypeUnion> pagedUnions = employeeService.listPagedEmployeeWorkTypes(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<EmployeeWorkTypeVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (EmployeeWorkTypeUnion union : pagedUnions.getData()) {
            EmployeeWorkTypeVo vo = buildEmployeeWorkTypeVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateEmployee(EmployeeCreateOrModifyParams params) {
        checkEmployeCreateParams(params);
        EmployeeUnion union = buildEmployeeUnionByParams(params);
        employeeService.createEmployee(union);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveEmployee(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择离职员工");
        employeeService.removeEmployee(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/remove", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveEmployees(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择离职员工");
        employeeService.removeEmployees(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/modify", method = { RequestMethod.POST })
    public JsonResult<Void> doModifyEmployee(@RequestBody EmployeeCreateOrModifyParams params) {
        checkEmployeModifyParams(params);
        EmployeeUnion union = buildEmployeeUnionByParams(params);
        employeeService.modifyEmployee(union);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<EmployeeVo> doQueryEmployeeDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "员工标识为空");
        EmployeeUnion union = employeeService.queryEmployeeDetail(id);
        EmployeeVo vo = buildEmployeeVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/list", method = { RequestMethod.GET })
    public JsonResult<PagedList<EmployeeVo>> doListPagedEmployees(@RequestBody EmployeeWorkQueryParams params) {
        checkEmployeeQueryParams(params);
        EmployeeWorkQueryCondition condition = CommonConverter.map(params, EmployeeWorkQueryCondition.class);
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

    private EmployeeWorkTypeVo buildEmployeeWorkTypeVoByUnion(EmployeeWorkTypeUnion union) {
        EmployeeWorkTypeVo vo = CommonConverter.map(union.getEmployeeWorkTypePo(), EmployeeWorkTypeVo.class);
        vo.setTopTypeDesc(EnumUtils.getDescByKey(union.getEmployeeWorkTypePo().getTopType(), TopTypeEnums.class));
        vo.setStoreName(union.getStoreName());
        return vo;
    }

    private EmployeeUnion buildEmployeeUnionByParams(EmployeeCreateOrModifyParams params) {
        EmployeePo employeePo = CommonConverter.map(params, EmployeePo.class);
        List<EmployeeWorkUnion> workUnions = Lists.newArrayListWithCapacity(params.getEmployeeWorkTypeIds().size());
        for (Long workTypeId : params.getEmployeeWorkTypeIds()) {
            EmployeeWorkPo workPo = new EmployeeWorkPo();
            workPo.setEmployeeWorkTypeId(workTypeId);;
            EmployeeWorkUnion workUnion = new EmployeeWorkUnion();
            workUnion.setEmployeeWorkPo(workPo);
            workUnions.add(workUnion);
        }
        EmployeeUnion union = new EmployeeUnion();
        union.setEmployeePo(employeePo);
        union.setEmployeeWorkUnions(workUnions);
        return union;
    }

    private void checkEmployeCreateParams(EmployeeCreateOrModifyParams params) {
        ensureParameterExist(params, "员工信息为空");
        ensureParameterNotExist(params.getId(), "员工标识应为空");
        ensureParameterExist(params.getName(), "员工姓名为空");
        ensureParameterExist(params.getGender(), "员工性别为空");
        ensureParameterValid(EnumUtils.containsKey(params.getGender(), GenderEnum.class), "员工性别错误");
        ensureParameterExist(params.getMobile(), "员工手机号为空");
        ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "员工手机号格式错误");
        if (params.getEntryTime() != null) {
            ensureParameterValid(params.getEntryTime().before(new Date()), "员工入职时间错误");
        }
        ensureCollectionNotEmpty(params.getEmployeeWorkTypeIds(), "员工工种为空");
    }

    private void checkEmployeModifyParams(EmployeeCreateOrModifyParams params) {
        ensureParameterExist(params, "员工修改信息为空");
        ensureParameterExist(params.getId(), "员工标识为空");
        if (params.getGender() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getGender(), GenderEnum.class), "员工性别错误");
        }
        if (Strings.isNullOrEmpty(params.getMobile())) {
            ensureParameterValid(CheckHelper.isValidMobile(params.getMobile()), "员工手机号格式错误");
        }
        if (params.getEntryTime() != null) {
            ensureParameterValid(params.getEntryTime().before(new Date()), "员工入职时间错误");
        }
    }

    private void checkEmployeeQueryParams(EmployeeWorkQueryParams params) {
        CheckHelper.checkPageParams(params);
        if (params.getStartEntryTime() != null) {
            ensureParameterValid(params.getStartEntryTime().before(new Date()), "员工入职查询时间段不能晚于现在");
        }
        if (params.getEndEntryTime() != null) {
            ensureParameterValid(params.getEndEntryTime().before(new Date()), "员工入职查询时间段不能晚于现在");
        }
        if (params.getStartEntryTime() != null && params.getEndEntryTime() != null) {
            ensureParameterValid(params.getStartEntryTime().before(params.getEndEntryTime()), "员工入职查询起止时间段错误");
        }
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnums.class), "员工顶层分类错误");
        }
    }

    private EmployeeVo buildEmployeeVoByUnion(EmployeeUnion union) {
        EmployeeVo vo = CommonConverter.map(union.getEmployeePo(), EmployeeVo.class);
        vo.setGender(EnumUtils.getDescByKey(union.getEmployeePo().getGender(), GenderEnum.class));
        vo.setWorking(EnumUtils.getDescByKey(union.getEmployeePo().getWorking(), WorkingEnums.class));
        List<EmployeeWorkVo> workVos = Lists.newArrayListWithCapacity(union.getEmployeeWorkUnions().size());
        for (EmployeeWorkUnion workUnion : union.getEmployeeWorkUnions()) {
            EmployeeWorkVo workVo = CommonConverter.map(workUnion.getEmployeeWorkTypePo(), EmployeeWorkVo.class);
            workVo.setEmployeeWorkTypeId(workUnion.getEmployeeWorkTypePo().getId());
            workVo.setTopTypeDesc(
                    EnumUtils.getDescByKey(workUnion.getEmployeeWorkTypePo().getTopType(), TopTypeEnums.class));
            workVos.add(workVo);
        }
        vo.setEmployeeWorkVos(workVos);
        return vo;
    }

}
