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
import com.zes.squad.gmh.entity.union.ProjectStockParams;
import com.zes.squad.gmh.entity.union.ProjectStockUnion;
import com.zes.squad.gmh.entity.union.ProjectTypeUnion;
import com.zes.squad.gmh.entity.union.ProjectUnion;
import com.zes.squad.gmh.service.ProjectService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ProjectCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.ProjectQueryParams;
import com.zes.squad.gmh.web.entity.param.ProjectTypeCreateOrModifyParams;
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
    public JsonResult<ProjectTypeVo> doCreateProjectType(@RequestBody ProjectTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "项目分类为空");
        ensureParameterNotExist(params.getId(), "项目分类标识应为空");
        ensureParameterExist(params.getTopType(), "项目分类顶层分类为空");
        ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnum.class), "项目分类顶层分类错误");
        ensureParameterExist(params.getName(), "项目分类名称为空");
        ProjectTypePo po = CommonConverter.map(params, ProjectTypePo.class);
        projectService.createProjectType(po);
        return JsonResults.success();
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

    @RequestMapping(path = "/type/modify", method = { RequestMethod.POST })
    public JsonResult<ProjectTypeVo> doModifyProjectType(@RequestBody ProjectTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "项目分类为空");
        ensureParameterExist(params.getId(), "库存分类标识为空");
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnum.class), "项目分类顶层分类错误");
        }
        ProjectTypePo po = CommonConverter.map(params, ProjectTypePo.class);
        projectService.modifyProjectType(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/{id}", method = { RequestMethod.GET })
    public JsonResult<ProjectTypeVo> doQueryProjectTypeDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "项目分类标识为空");
        ProjectTypeUnion union = projectService.queryProjectTypeDetail(id);
        ProjectTypeVo vo = buildProjectTypeVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/type/list", method = { RequestMethod.PUT })
    public JsonResult<PagedList<ProjectTypeVo>> doListPagedProjectTypes(@RequestBody ProjectTypeQueryParams params) {
        CheckHelper.checkPageParams(params);
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnum.class), "项目分类顶层分类错误");
        }
        ProjectTypeQueryCondition condition = CommonConverter.map(params, ProjectTypeQueryCondition.class);
        PagedList<ProjectTypeUnion> pagedUnions = projectService.listPagedProjectTypes(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ProjectTypeVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (ProjectTypeUnion union : pagedUnions.getData()) {
            ProjectTypeVo vo = buildProjectTypeVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateProject(@RequestBody ProjectCreateOrModifyParams params) {
        checkProjectCreateParams(params);
        ProjectUnion union = buildProjectUnionByCreateOrModifyParams(params);
        projectService.createProject(union);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveProject(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择要删除的项目");
        projectService.removeProject(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/remove", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveProjects(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择要删除的项目");
        projectService.removeProjects(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/modify", method = { RequestMethod.POST })
    public JsonResult<Void> doModifyProject(@RequestBody ProjectCreateOrModifyParams params) {
        checkProjectModifyParams(params);
        ProjectUnion union = buildProjectUnionByCreateOrModifyParams(params);
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

    @RequestMapping(path = "/list", method = { RequestMethod.PUT })
    public JsonResult<PagedList<ProjectVo>> doListPagedProjects(@RequestBody ProjectQueryParams params) {
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

    private ProjectTypeVo buildProjectTypeVoByUnion(ProjectTypeUnion union) {
        ProjectTypeVo vo = CommonConverter.map(union.getProjectTypePo(), ProjectTypeVo.class);
        vo.setTopTypeDesc(EnumUtils.getDescByKey(union.getProjectTypePo().getTopType(), TopTypeEnum.class));
        vo.setStoreName(union.getStorePo().getName());
        return vo;
    }

    private void checkProjectCreateParams(ProjectCreateOrModifyParams params) {
        ensureParameterExist(params, "项目信息为空");
        ensureParameterNotExist(params.getId(), "项目标识应为空");
        ensureParameterExist(params.getProjectTypeId(), "项目分类为空");
        ensureParameterExist(params.getName(), "项目名称为空");
        ensureParameterExist(params.getUnitPrice(), "项目单价为空");
        ensureParameterValid(params.getUnitPrice().compareTo(BigDecimal.ZERO) == 1, "项目单价非法");
        ensureParameterExist(params.getIntegral(), "项目积分为空");
        ensureParameterValid(params.getIntegral().compareTo(BigDecimal.ZERO) == 1, "项目积分非法");
        ensureParameterExist(params.getInternIntegral(), "项目实习生积分为空");
        ensureParameterValid(params.getInternIntegral().compareTo(BigDecimal.ZERO) == 1, "项目实习生积分非法");
        ensureParameterValid(params.getIntegral().compareTo(params.getInternIntegral()) == 1, "项目积分应大于实习生积分");
        ensureCollectionNotEmpty(params.getProjectStockParams(), "项目所需库存为空");
        for (ProjectStockParams stockParams : params.getProjectStockParams()) {
            ensureParameterNotExist(stockParams.getStockId(), "项目所需库存标识应为空");
            ensureParameterValid(stockParams.getStockConsumeAmount().compareTo(BigDecimal.ZERO) == 1, "项目所需库存量错误");
        }
    }

    private ProjectUnion buildProjectUnionByCreateOrModifyParams(ProjectCreateOrModifyParams params) {
        ProjectPo projectPo = CommonConverter.map(params, ProjectPo.class);
        ProjectUnion union = new ProjectUnion();
        union.setProjectPo(projectPo);
        if (CollectionUtils.isNotEmpty(params.getProjectStockParams())) {
            List<ProjectStockUnion> stockUnions = Lists.newArrayListWithCapacity(params.getProjectStockParams().size());
            for (ProjectStockParams stockParam : params.getProjectStockParams()) {
                ProjectStockPo stockPo = CommonConverter.map(stockParam, ProjectStockPo.class);
                ProjectStockUnion stockUnion = new ProjectStockUnion();
                stockUnion.setProjectStockPo(stockPo);
                stockUnions.add(stockUnion);
            }
            union.setProjectStockUnions(stockUnions);
        }
        return union;
    }

    private void checkProjectModifyParams(ProjectCreateOrModifyParams params) {
        ensureParameterExist(params, "项目信息为空");
        ensureParameterExist(params.getId(), "项目标识应为空");
        if (params.getUnitPrice() != null) {
            ensureParameterValid(params.getUnitPrice().compareTo(BigDecimal.ZERO) == 1, "项目单价非法");
        }
        if (params.getIntegral() != null) {
            ensureParameterValid(params.getIntegral().compareTo(BigDecimal.ZERO) == 1, "项目积分非法");
        }
        if (params.getInternIntegral() != null) {
            ensureParameterValid(params.getInternIntegral().compareTo(BigDecimal.ZERO) == 1, "项目实习生积分非法");
        }
        if (params.getIntegral() != null && params.getInternIntegral() != null) {
            ensureParameterValid(params.getIntegral().compareTo(params.getInternIntegral()) == 1, "项目积分应大于实习生积分");
        }
        if (CollectionUtils.isNotEmpty(params.getProjectStockParams())) {
            for (ProjectStockParams stockParams : params.getProjectStockParams()) {
                ensureParameterNotExist(stockParams.getStockId(), "项目所需库存标识应为空");
                ensureParameterValid(stockParams.getStockConsumeAmount().compareTo(BigDecimal.ZERO) == 1, "项目所需库存量错误");
            }
        }
    }

    private ProjectVo buildProjectVoByUnion(ProjectUnion union) {
        ProjectVo vo = CommonConverter.map(union.getProjectPo(), ProjectVo.class);
        vo.setStoreName(union.getStorePo().getName());
        List<ProjectStockVo> stockVos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion stockUnion : union.getProjectStockUnions()) {
            ProjectStockVo stockVo = new ProjectStockVo();
            stockVo.setStockName(stockUnion.getStockPo().getName());
            stockVo.setStockConsumeAmount(stockUnion.getProjectStockPo().getStockConsumeAmount());
            stockVos.add(stockVo);
        }
        vo.setProjectStockVos(stockVos);
        return vo;
    }

}
