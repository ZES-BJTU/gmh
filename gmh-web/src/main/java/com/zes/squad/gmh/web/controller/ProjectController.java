package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.math.BigDecimal;
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

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.enums.TopTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.union.ProjectStockUnion;
import com.zes.squad.gmh.entity.union.ProjectUnion;
import com.zes.squad.gmh.service.ProjectService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ProjectParams;
import com.zes.squad.gmh.web.entity.param.ProjectQueryParams;
import com.zes.squad.gmh.web.entity.param.ProjectStockParams;
import com.zes.squad.gmh.web.entity.param.ProjectTypeParams;
import com.zes.squad.gmh.web.entity.param.ProjectTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.ProjectStockVo;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.entity.vo.ProjectVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/projects")
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(path = "/types", method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ProjectTypeVo> doCreateProjectType(@RequestBody ProjectTypeParams params) {
        ensureParameterExist(params, "项目分类为空");
        ensureParameterNotExist(params.getId(), "项目分类已存在");
        ensureParameterExist(params.getTopType(), "项目分类顶层分类为空");
        ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnum.class), "项目分类顶层分类错误");
        ensureParameterExist(params.getName(), "项目分类名称为空");
        ProjectTypePo po = CommonConverter.map(params, ProjectTypePo.class);
        ProjectTypePo newPo = projectService.createProjectType(po);
        ProjectTypeVo vo = CommonConverter.map(newPo, ProjectTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProjectType(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除项目分类");
        projectService.removeProjectType(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/types", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProjectTypes(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除项目分类");
        projectService.removeProjectTypes(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.PUT })
    public JsonResult<ProjectTypeVo> doModifyProjectType(@PathVariable("id") Long id,
                                                         @RequestBody ProjectTypeParams params) {
        ensureParameterExist(params, "项目分类为空");
        ensureParameterExist(id, "项目分类为空");
        params.setId(id);
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnum.class), "项目分类顶层分类错误");
        }
        ProjectTypePo po = CommonConverter.map(params, ProjectTypePo.class);
        ProjectTypePo newPo = projectService.modifyProjectType(po);
        ProjectTypeVo vo = CommonConverter.map(newPo, ProjectTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.GET })
    public JsonResult<ProjectTypeVo> doQueryProjectTypeDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "项目分类不存在");
        ProjectTypePo po = projectService.queryProjectTypeDetail(id);
        ProjectTypeVo vo = buildProjectTypeVoByPo(po);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types", method = { RequestMethod.GET })
    public JsonResult<PagedList<ProjectTypeVo>> doListPagedProjectTypes(ProjectTypeQueryParams params) {
        CheckHelper.checkPageParams(params);
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnum.class), "项目分类顶层分类错误");
        }
        ProjectTypeQueryCondition condition = CommonConverter.map(params, ProjectTypeQueryCondition.class);
        PagedList<ProjectTypePo> pagedPos = projectService.listPagedProjectTypes(condition);
        if (CollectionUtils.isEmpty(pagedPos.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedPos.getPageNum(), pagedPos.getPageSize()));
        }
        List<ProjectTypeVo> vos = Lists.newArrayListWithCapacity(pagedPos.getData().size());
        for (ProjectTypePo po : pagedPos.getData()) {
            ProjectTypeVo vo = buildProjectTypeVoByPo(po);
            vos.add(vo);
        }
        return JsonResults.success(
                PagedLists.newPagedList(pagedPos.getPageNum(), pagedPos.getPageSize(), pagedPos.getTotalCount(), vos));
    }

    @RequestMapping(path = "/types/all", method = { RequestMethod.GET })
    public JsonResult<List<ProjectTypeVo>> doListAllProjectTypes() {
        List<ProjectTypePo> pos = projectService.listAllProjectTypes();
        if (CollectionUtils.isEmpty(pos)) {
            return JsonResults.success(Lists.newArrayList());
        }
        List<ProjectTypeVo> vos = Lists.newArrayListWithCapacity(pos.size());
        for (ProjectTypePo po : pos) {
            ProjectTypeVo vo = buildProjectTypeVoByPo(po);
            vos.add(vo);
        }
        return JsonResults.success(vos);
    }

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ProjectVo> doCreateProject(@RequestBody ProjectParams params) {
        checkProjectCreateParams(params);
        ProjectUnion union = buildProjectUnionByParams(params);
        ProjectUnion newUnion = projectService.createProject(union);
        ProjectVo vo = buildProjectVoByUnion(newUnion);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProject(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择要删除的项目");
        projectService.removeProject(id);
        return JsonResults.success();
    }

    @RequestMapping(method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProjects(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择要删除的项目");
        projectService.removeProjects(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<Void> doModifyProject(@PathVariable("id") Long id, @RequestBody ProjectParams params) {
        ensureParameterExist(id, "项目不存在");
        params.setId(id);
        checkProjectModifyParams(params);
        ProjectUnion union = buildProjectUnionByParams(params);
        projectService.modifyProject(union);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<ProjectVo> doQueryProjectDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待查看的项目");
        ProjectUnion union = projectService.queryProjectDetail(id);
        ProjectVo vo = buildProjectVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<ProjectVo>> doListPagedProjects(ProjectQueryParams params) {
        CheckHelper.checkPageParams(params);
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnum.class), "顶层分类错误");
        }
        ProjectQueryCondition condition = CommonConverter.map(params, ProjectQueryCondition.class);
        PagedList<ProjectUnion> pagedUnions = projectService.listPagedProjects(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ProjectVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (ProjectUnion union : pagedUnions.getData()) {
            ProjectVo vo = buildProjectVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    private ProjectTypeVo buildProjectTypeVoByPo(ProjectTypePo po) {
        ProjectTypeVo vo = CommonConverter.map(po, ProjectTypeVo.class);
        vo.setTopTypeDesc(EnumUtils.getDescByKey(po.getTopType(), TopTypeEnum.class));
        return vo;
    }

    private void checkProjectCreateParams(ProjectParams params) {
        ensureParameterExist(params, "项目信息为空");
        ensureParameterNotExist(params.getId(), "项目已存在");
        ensureParameterExist(params.getProjectTypeId(), "项目分类为空");
        ensureParameterExist(params.getCode(), "项目代码为空");
        ensureParameterExist(params.getName(), "项目名称为空");
        ensureParameterExist(params.getUnitPrice(), "项目单价为空");
        ensureParameterValid(params.getUnitPrice().compareTo(BigDecimal.ZERO) == 1, "项目单价应大于0");
        ensureParameterExist(params.getIntegral(), "项目积分为空");
        ensureParameterValid(params.getIntegral().compareTo(BigDecimal.ZERO) == 1, "项目积分应大于0");
        ensureParameterExist(params.getInternIntegral(), "项目实习生积分为空");
        ensureParameterValid(params.getInternIntegral().compareTo(BigDecimal.ZERO) == 1, "项目实习生积分应大于0");
        ensureCollectionNotEmpty(params.getProjectStockParams(), "项目所需库存为空");
        for (ProjectStockParams stockParams : params.getProjectStockParams()) {
            ensureParameterExist(stockParams.getStockId(), "项目所需库存为空");
            ensureParameterExist(stockParams.getStockConsumptionAmount(), "项目所需库存量为空");
            ensureParameterValid(stockParams.getStockConsumptionAmount().compareTo(BigDecimal.ZERO) == 1,
                    "项目所需库存量应大于0");
        }
    }

    private ProjectUnion buildProjectUnionByParams(ProjectParams params) {
        ProjectPo projectPo = CommonConverter.map(params, ProjectPo.class);
        ProjectUnion union = new ProjectUnion();
        union.setProjectPo(projectPo);
        List<ProjectStockUnion> stockUnions = Lists.newArrayListWithCapacity(params.getProjectStockParams().size());
        for (ProjectStockParams stockParam : params.getProjectStockParams()) {
            ProjectStockPo stockPo = CommonConverter.map(stockParam, ProjectStockPo.class);
            ProjectStockUnion stockUnion = new ProjectStockUnion();
            stockUnion.setProjectStockPo(stockPo);
            stockUnions.add(stockUnion);
        }
        union.setProjectStockUnions(stockUnions);
        return union;
    }

    private void checkProjectModifyParams(ProjectParams params) {
        ensureParameterExist(params, "项目信息为空");
        ensureParameterExist(params.getId(), "项目不存在");
        if (params.getUnitPrice() != null) {
            ensureParameterValid(params.getUnitPrice().compareTo(BigDecimal.ZERO) == 1, "项目单价应大于0");
        }
        if (params.getIntegral() != null) {
            ensureParameterValid(params.getIntegral().compareTo(BigDecimal.ZERO) == 1, "项目积分应大于0");
        }
        if (params.getInternIntegral() != null) {
            ensureParameterValid(params.getInternIntegral().compareTo(BigDecimal.ZERO) == 1, "项目实习生积分应大于0");
        }
        if (CollectionUtils.isNotEmpty(params.getProjectStockParams())) {
            for (ProjectStockParams stockParams : params.getProjectStockParams()) {
                ensureParameterNotExist(stockParams.getStockId(), "项目所需库存为空");
                ensureParameterNotExist(stockParams.getStockConsumptionAmount(), "项目所需库存量为空");
                ensureParameterValid(stockParams.getStockConsumptionAmount().compareTo(BigDecimal.ZERO) == 1,
                        "项目所需库存量应大于0");
            }
        }
    }

    private ProjectVo buildProjectVoByUnion(ProjectUnion union) {
        ProjectVo vo = CommonConverter.map(union.getProjectPo(), ProjectVo.class);
        vo.setTopTypeDesc(EnumUtils.getDescByKey(union.getProjectTypePo().getTopType(), TopTypeEnum.class));
        vo.setProjectTypeName(union.getProjectTypePo().getName());
        List<ProjectStockVo> stockVos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion projectStockUnion : union.getProjectStockUnions()) {
            ProjectStockVo stockVo = new ProjectStockVo();
            stockVo.setStockId(projectStockUnion.getProjectStockPo().getId());
            stockVo.setStockName(projectStockUnion.getStockUnion().getStockPo().getName());
            stockVo.setStockConsumptionAmount(projectStockUnion.getProjectStockPo().getStockConsumptionAmount());
            stockVo.setUnitName(projectStockUnion.getStockUnion().getStockPo().getUnitName());
            stockVos.add(stockVo);
        }
        vo.setProjectStockVos(stockVos);
        return vo;
    }

}
