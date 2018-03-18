package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureAttributeExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionValid;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProjectQueryCondition;
import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectPo;
import com.zes.squad.gmh.entity.po.ProjectStockPo;
import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.union.ProjectStockUnion;
import com.zes.squad.gmh.entity.union.ProjectUnion;
import com.zes.squad.gmh.mapper.ProjectMapper;
import com.zes.squad.gmh.mapper.ProjectStockMapper;
import com.zes.squad.gmh.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.mapper.ProjectUnionMapper;
import com.zes.squad.gmh.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectTypeMapper  projectTypeMapper;
    @Autowired
    private ProjectMapper      projectMapper;
    @Autowired
    private ProjectUnionMapper projectUnionMapper;
    @Autowired
    private ProjectStockMapper projectStockMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectTypePo createProjectType(ProjectTypePo po) {
        List<ProjectTypePo> pos = projectTypeMapper.selectByTopTypeAndName(po.getTopType(), po.getName());
        ensureCollectionNotEmpty(pos, "项目分类已存在");
        projectTypeMapper.insert(po);
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjectType(Long id) {
        int row = projectTypeMapper.deleteById(id);
        ensureConditionValid(row == 1, "项目分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjectTypes(List<Long> ids) {
        int rows = projectTypeMapper.batchDelete(ids);
        ensureConditionValid(rows == ids.size(), "项目分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectTypePo modifyProjectType(ProjectTypePo po) {
        projectTypeMapper.updateSelective(po);
        ProjectTypePo newPo = projectTypeMapper.selectById(po.getId());
        ensureEntityExist(newPo, "项目分类不存在");
        return newPo;
    }

    @Override
    public ProjectTypePo queryProjectTypeDetail(Long id) {
        ProjectTypePo po = projectTypeMapper.selectById(id);
        ensureEntityExist(po, "未找到项目分类");
        return po;
    }

    @Override
    public PagedList<ProjectTypePo> listPagedProjectTypes(ProjectTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProjectTypePo> unions = projectTypeMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<ProjectTypePo> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectUnion createProject(ProjectUnion union) {
        ProjectPo projectPo = union.getProjectPo();
        ProjectPo po = projectMapper.selectByCode(projectPo.getCode());
        ensureEntityNotExist(po, "项目已存在");
        projectMapper.insert(projectPo);
        Long projectId = projectPo.getId();
        ensureAttributeExist(projectId, "添加项目失败");
        List<ProjectStockPo> stockPos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion stockUnion : union.getProjectStockUnions()) {
            ProjectStockPo stockPo = stockUnion.getProjectStockPo();
            stockPo.setProjectId(projectId);
            stockPos.add(stockPo);
        }
        int rows = projectStockMapper.batchInsert(stockPos);
        ensureConditionValid(rows == stockPos.size(), "添加项目失败");
        ProjectUnion newUnion = new ProjectUnion();
        newUnion.setId(projectPo.getId());
        newUnion.setProjectPo(projectPo);
        List<ProjectStockPo> newStockPos = projectStockMapper.selectByProjectId(projectId);
        ensureCollectionNotEmpty(newStockPos, "添加项目失败");
        List<ProjectStockUnion> newStockUnions = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockPo stockPo : newStockPos) {
            ProjectStockUnion stockUnion = new ProjectStockUnion();
            stockUnion.setId(stockPo.getId());
            stockUnion.setProjectStockPo(stockPo);
            newStockUnions.add(stockUnion);
        }
        newUnion.setProjectStockUnions(newStockUnions);
        return newUnion;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProject(Long id) {
        int row = projectMapper.deleteById(id);
        ensureConditionValid(row == 1, "删除项目失败");
        projectStockMapper.batchDeleteByProjectIds(Lists.newArrayList(id));
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeProjects(List<Long> ids) {
        int rows = projectMapper.batchDelete(ids);
        ensureConditionValid(rows == ids.size(), "删除项目失败");
        projectStockMapper.batchDeleteByProjectIds(ids);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public ProjectUnion modifyProject(ProjectUnion union) {
        ProjectPo projectPo = union.getProjectPo();
        Long projectId = projectPo.getId();
        String code = projectPo.getCode();
        if (!Strings.isNullOrEmpty(code)) {
            ProjectPo po = projectMapper.selectByCode(code);
            if (po != null) {
                ensureConditionValid(po.getId().equals(projectId), "会员卡编码重复");
            }
        }
        int row = projectMapper.updateSelective(projectPo);
        ensureConditionValid(row == 1, "修改项目失败");
        ProjectPo newPo = projectMapper.selectById(projectId);
        ensureEntityExist(newPo, "项目不存在");
        projectStockMapper.batchDeleteByProjectIds(Lists.newArrayList(projectId));
        List<ProjectStockPo> stockPos = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockUnion stockUnion : union.getProjectStockUnions()) {
            ProjectStockPo stockPo = stockUnion.getProjectStockPo();
            stockPo.setProjectId(projectId);
            stockPos.add(stockPo);
        }
        int rows = projectStockMapper.batchInsert(stockPos);
        ensureConditionValid(rows == stockPos.size(), "修改项目失败");
        ProjectUnion newUnion = new ProjectUnion();
        newUnion.setId(projectId);
        newUnion.setProjectPo(newPo);
        List<ProjectStockPo> newStockPos = projectStockMapper.selectByProjectId(projectId);
        ensureCollectionNotEmpty(newStockPos, "添加项目失败");
        List<ProjectStockUnion> newStockUnions = Lists.newArrayListWithCapacity(union.getProjectStockUnions().size());
        for (ProjectStockPo stockPo : newStockPos) {
            ProjectStockUnion stockUnion = new ProjectStockUnion();
            stockUnion.setId(stockPo.getId());
            stockUnion.setProjectStockPo(stockPo);
            newStockUnions.add(stockUnion);
        }
        newUnion.setProjectStockUnions(newStockUnions);
        return newUnion;
    }

    @Override
    public ProjectUnion queryProjectDetail(Long id) {
        ProjectUnion union = projectUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到项目");
        ensureAttributeExist(union.getId(), "未找到项目");
        ensureEntityExist(union.getProjectTypePo(), "未找到项目分类");
        ensureEntityExist(union.getProjectPo(), "未找到项目");
        ensureCollectionNotEmpty(union.getProjectStockUnions(), "未找到项目对应库存");
        return union;
    }

    @Override
    public Long queryProjectByCode(String code) {
        ensureParameterExist(code, "项目编码为空");
        ProjectPo po = projectMapper.selectByCode(code);
        if (po == null) {
            return null;
        }
        return po.getId();
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
