package com.zes.squad.gmh.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
import com.zes.squad.gmh.entity.condition.EmployeeWorkTypeQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeePo;
import com.zes.squad.gmh.entity.po.EmployeeWorkPo;
import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;
import com.zes.squad.gmh.entity.union.EmployeeUnion;
import com.zes.squad.gmh.entity.union.EmployeeWorkTypeUnion;
import com.zes.squad.gmh.entity.union.EmployeeWorkUnion;
import com.zes.squad.gmh.mapper.EmployeeMapper;
import com.zes.squad.gmh.mapper.EmployeeUnionMapper;
import com.zes.squad.gmh.mapper.EmployeeWorkMapper;
import com.zes.squad.gmh.mapper.EmployeeWorkTypeMapper;
import com.zes.squad.gmh.mapper.EmployeeWorkTypeUnionMapper;
import com.zes.squad.gmh.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeWorkTypeMapper      employeeWorkTypeMapper;
    @Autowired
    private EmployeeWorkTypeUnionMapper employeeWorkTypeUnionMapper;
    @Autowired
    private EmployeeMapper              employeeMapper;
    @Autowired
    private EmployeeWorkMapper          employeeWorkMapper;
    @Autowired
    private EmployeeUnionMapper         employeeUnionMapper;

    @Override
    public void createEmployeeWorkType(EmployeeWorkTypePo po) {
        po.setStoreId(ThreadContext.getUserStoreId());
        employeeWorkTypeMapper.insert(po);
    }

    @Override
    public void removeEmployeeWrokType(Long id) {
        employeeWorkTypeMapper.deleteById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void removeEmployeeWorkTypes(List<Long> ids) {
        employeeWorkTypeMapper.batchDelete(ids);
    }

    @Override
    public void modifyEmployeeWorkType(EmployeeWorkTypePo po) {
        employeeWorkTypeMapper.updateSelective(po);
    }

    @Override
    public EmployeeWorkTypeUnion queryEmployeeWorkTypeDetail(Long id) {
        EmployeeWorkTypeUnion union = employeeWorkTypeUnionMapper.selectById(id);
        ensureEntityExist(union, "获取员工工种失败");
        ensureEntityExist(union.getEmployeeWorkTypePo(), "获取员工工种失败");
        return union;
    }

    @Override
    public PagedList<EmployeeWorkTypeUnion> listPagedEmployeeWorkTypes(EmployeeWorkTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<EmployeeWorkTypeUnion> unions = employeeWorkTypeUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<EmployeeWorkTypeUnion> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void createEmployee(EmployeeUnion union) {
        EmployeePo employeePo = union.getEmployeePo();
        if (employeePo.getEntryTime() == null) {
            employeePo.setEntryTime(new Date());
        }
        employeePo.setStoreId(ThreadContext.getUserStoreId());
        employeeMapper.insert(employeePo);
        ensureParameterExist(employeePo.getId(), "添加员工失败");
        List<EmployeeWorkPo> workPos = Lists.newArrayListWithCapacity(union.getEmployeeWorkUnions().size());
        for (EmployeeWorkUnion workUnion : union.getEmployeeWorkUnions()) {
            EmployeeWorkPo workPo = new EmployeeWorkPo();
            workPo.setEmployeeId(employeePo.getId());
            workPo.setEmployeeWorkTypeId(workUnion.getEmployeeWorkPo().getEmployeeWorkTypeId());
            workPos.add(workPo);
        }
        employeeWorkMapper.batchInsert(workPos);
    }

    @Override
    public void removeEmployee(Long id) {
        employeeMapper.updateWorkingById(id);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeEmployees(List<Long> ids) {
        employeeMapper.batchUpdateWorkingByIds(ids);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void modifyEmployee(EmployeeUnion union) {
        EmployeePo employeePo = union.getEmployeePo();
        Long employeeId = employeePo.getId();
        employeeWorkMapper.batchDelete(employeeId);
        employeePo.setId(null);
        employeeMapper.insert(employeePo);
        ensureParameterExist(employeePo.getId(), "修改员工信息失败");
        List<EmployeeWorkPo> workPos = Lists.newArrayListWithCapacity(union.getEmployeeWorkUnions().size());
        for (EmployeeWorkUnion workUnion : union.getEmployeeWorkUnions()) {
            EmployeeWorkPo workPo = new EmployeeWorkPo();
            workPo.setEmployeeId(employeePo.getId());
            workPo.setEmployeeWorkTypeId(workUnion.getEmployeeWorkPo().getEmployeeWorkTypeId());
            workPos.add(workPo);
        }
        employeeWorkMapper.batchInsert(workPos);
    }

    @Override
    public EmployeeUnion queryEmployeeDetail(Long id) {
        EmployeeUnion union = employeeUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到员工详细信息");
        ensureEntityExist(union.getEmployeePo(), "未找到员工详细信息");
        ensureCollectionNotEmpty(union.getEmployeeWorkUnions(), "未找到员工工种信息");
        return union;
    }

    @Override
    public PagedList<EmployeeUnion> listPagedEmployees(EmployeeWorkQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<Long> ids = employeeWorkMapper.selectEmployeeIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        List<EmployeeUnion> unions = employeeUnionMapper.selectByIds(ids);
        PageInfo<Long> info = new PageInfo<>(ids);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
