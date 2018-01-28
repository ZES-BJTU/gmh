package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
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
import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.union.ProjectTypeUnion;
import com.zes.squad.gmh.service.ProjectService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.project.ProjectTypeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.project.ProjectTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.ProjectTypeVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/project")
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(path = "/type/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateProjectType(@RequestBody ProjectTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "项目分类为空");
        ensureParameterNotExist(params.getId(), "项目分类标识应为空");
        ensureParameterExist(params.getTopType(), "项目分类顶层分类为空");
        ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnums.class), "项目分类顶层分类错误");
        ensureParameterExist(params.getName(), "项目分类名称为空");
        ProjectTypePo po = CommonConverter.map(params, ProjectTypePo.class);
        projectService.createProjectType(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/{id}", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveProjectType(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除项目分类");
        projectService.removeProjectType(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/remove", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveProjectTypes(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除项目分类");
        projectService.removeProjectTypes(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/modify", method = { RequestMethod.POST })
    public JsonResult<Void> doModifyProjectType(@RequestBody ProjectTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "项目分类为空");
        ensureParameterExist(params.getId(), "库存分类标识为空");
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnums.class), "项目分类顶层分类错误");
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

    @RequestMapping(path = "/type/create", method = { RequestMethod.PUT })
    public JsonResult<PagedList<ProjectTypeVo>> doListPagedProjectTypes(@RequestBody ProjectTypeQueryParams params) {
        CheckHelper.checkPageParams(params);
        if (params.getTopType() != null) {
            ensureParameterValid(EnumUtils.containsKey(params.getTopType(), TopTypeEnums.class), "项目分类顶层分类错误");
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

    private ProjectTypeVo buildProjectTypeVoByUnion(ProjectTypeUnion union) {
        ProjectTypeVo vo = CommonConverter.map(union.getProjectTypePo(), ProjectTypeVo.class);
        vo.setTopTypeDesc(EnumUtils.getDescByKey(union.getProjectTypePo().getTopType(), TopTypeEnums.class));
        vo.setStoreName(union.getStorePo().getName());
        return vo;
    }

}
