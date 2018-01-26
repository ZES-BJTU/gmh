package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.TopTypeEnums;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.EmployeeWorkTypeQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;
import com.zes.squad.gmh.entity.union.EmployeeWorkTypeUnion;
import com.zes.squad.gmh.service.EmployeeService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.employee.EmployeeWorkTypeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.employee.EmployeeWorkTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.EmployeeWorkTypeVo;
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

    private EmployeeWorkTypeVo buildEmployeeWorkTypeVoByUnion(EmployeeWorkTypeUnion union) {
        EmployeeWorkTypeVo vo = CommonConverter.map(union.getEmployeeWorkTypePo(), EmployeeWorkTypeVo.class);
        vo.setTopTypeDesc(EnumUtils.getDescByKey(union.getEmployeeWorkTypePo().getTopType(), TopTypeEnums.class));
        vo.setStoreName(union.getStoreName());
        return vo;
    }

}
