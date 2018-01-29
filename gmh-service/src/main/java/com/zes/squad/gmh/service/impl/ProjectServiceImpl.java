package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureAttributeExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.union.ProjectStockUnion;
import com.zes.squad.gmh.entity.union.ProjectTypeUnion;
import com.zes.squad.gmh.entity.union.ProjectUnion;
import com.zes.squad.gmh.mapper.ProjectMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.mapper.ProjectTypeUnionMapper;
import com.zes.squad.gmh.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectTypeMapper      projectTypeMapper;
    @Autowired
    private ProjectTypeUnionMapper projectTypeUnionMapper;
    @Autowired
    private ProjectMapper          projectMapper;
    @Autowired
    private ProjectUnionMapper     projectUnionMapper;
    @Autowired
    private ProjectStockMapper     projectStockMapper;

    @Override
    public void createProjectType(ProjectTypePo po) {
        po.setStoreId(ThreadContext.getUserStoreId());
        projectTypeMapper.insert(po);
    }

    @Override
    public void removeProjectType(Long id) {
        projectTypeMapper.deleteById(id);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjectTypes(List<Long> ids) {
        projectTypeMapper.batchDelete(ids);
    }

    @Override
    public void modifyProjectType(ProjectTypePo po) {
        projectTypeMapper.updateSelective(po);
    }

    @Override
    public ProjectTypeUnion queryProjectTypeDetail(Long id) {
        ProjectTypeUnion union = projectTypeUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到项目分类");
        ensureAttributeExist(union.getId(), "未找到项目分类标识");
        ensureEntityExist(union.getProjectTypePo(), "未找到项目分类");
        ensureEntityExist(union.getStorePo(), "未找到项目分类所属门店");
        return union;
    }

    @Override
    public PagedList<ProjectTypeUnion> listPagedProjectTypes(ProjectTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProjectTypeUnion> unions = projectTypeUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProjectTypeUnion> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void createProject(ProjectUnion union) {
        ProjectPo projectPo = union.getProjectPo();
        projectMapper.insert(projectPo);
        Long projectId = projectPo.getId();
        ensureAttributeExist(projectId, "添加项目失败");
        List<ProjectStockPo> stockPos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion stockUnion : union.getProjectStockUnions()) {
            ProjectStockPo stockPo = stockUnion.getProjectStockPo();
            stockPo.setProjectId(projectId);
            stockPos.add(stockPo);
        }
        projectStockMapper.batchInsert(stockPos);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProject(Long id) {
        projectMapper.deleteById(id);
        projectStockMapper.batchDeleteByProjectIds(Lists.newArrayList(id));
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjects(List<Long> ids) {
        projectMapper.batchDelete(ids);
        projectStockMapper.batchDeleteByProjectIds(ids);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void modifyProject(ProjectUnion union) {
        ProjectPo projectPo = union.getProjectPo();
        projectMapper.updateSelective(projectPo);
        Long projectId = projectPo.getId();
        projectStockMapper.batchDeleteByProjectIds(Lists.newArrayList(projectId));
        List<ProjectStockPo> stockPos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion stockUnion : union.getProjectStockUnions()) {
            ProjectStockPo stockPo = stockUnion.getProjectStockPo();
            stockPo.setProjectId(projectId);
            stockPos.add(stockPo);
        }
        projectStockMapper.batchInsert(stockPos);
    }

    @Override
    public ProjectUnion queryProjectDetail(Long id) {
        ProjectUnion union = projectUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到项目");
        ensureAttributeExist(union.getId(), "未找到项目标识");
        ensureEntityExist(union.getProjectPo(), "未找到项目");
        ensureEntityExist(union.getStorePo(), "未找到项目所在门店");
        ensureCollectionNotEmpty(union.getProjectStockUnions(), "未找到项目对应库存");
        return union;
    }

    @Override
    public PagedList<ProjectUnion> listPagedProjects(ProjectQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Long> ids = projectMapper.selectIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        List<ProjectUnion> unions = projectUnionMapper.selectByIds(ids);
        PageInfo<Long> info = new PageInfo<>(ids);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
