package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.ProjectTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProjectTypePo;
import com.zes.squad.gmh.entity.union.ProjectTypeUnion;
import com.zes.squad.gmh.mapper.ProjectTypeMapper;
import com.zes.squad.gmh.mapper.ProjectTypeUnionMapper;
import com.zes.squad.gmh.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectTypeMapper      projectTypeMapper;
    @Autowired
    private ProjectTypeUnionMapper projectTypeUnionMapper;

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

}
