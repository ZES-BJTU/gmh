package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.util.Date;
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
import com.zes.squad.gmh.entity.condition.EmployeeWorkQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeePo;
import com.zes.squad.gmh.entity.po.EmployeeWorkPo;
import com.zes.squad.gmh.entity.union.EmployeeUnion;
import com.zes.squad.gmh.mapper.EmployeeMapper;
import com.zes.squad.gmh.mapper.EmployeeUnionMapper;
import com.zes.squad.gmh.mapper.EmployeeWorkMapper;
import com.zes.squad.gmh.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper      employeeMapper;
    @Autowired
    private EmployeeWorkMapper  employeeWorkMapper;
    @Autowired
    private EmployeeUnionMapper employeeUnionMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void createEmployee(EmployeeUnion union) {
        EmployeePo employeePo = union.getEmployeePo();
        if (employeePo.getEntryTime() == null) {
            employeePo.setEntryTime(new Date());
        }
        employeePo.setWorking(true);
        employeePo.setStoreId(ThreadContext.getUserStoreId());
        employeeMapper.insert(employeePo);
        ensureParameterExist(employeePo.getId(), "添加员工失败");
        List<EmployeeWorkPo> workPos = Lists.newArrayListWithCapacity(union.getEmployeeWorkPos().size());
        for (EmployeeWorkPo workPo : union.getEmployeeWorkPos()) {
            workPo.setEmployeeId(employeePo.getId());
            workPos.add(workPo);
        }
        employeeWorkMapper.batchInsert(workPos);
    }

    @Override
    public void removeEmployee(Long id) {
        employeeMapper.updateWorkingById(id);
        employeeWorkMapper.batchDelete(id);
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
        employeeMapper.updateSelective(employeePo);
        Long employeeId = employeePo.getId();
        employeeWorkMapper.batchDelete(employeeId);
        List<EmployeeWorkPo> workPos = Lists.newArrayListWithCapacity(union.getEmployeeWorkPos().size());
        for (EmployeeWorkPo workPo : union.getEmployeeWorkPos()) {
            workPo.setEmployeeId(employeePo.getId());
            workPos.add(workPo);
        }
        employeeWorkMapper.batchInsert(workPos);
    }

    @Override
    public EmployeeUnion queryEmployeeDetail(Long id) {
        EmployeeUnion union = employeeUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到员工详细信息");
        ensureEntityExist(union.getEmployeePo(), "未找到员工详细信息");
        ensureCollectionNotEmpty(union.getEmployeeWorkPos(), "未找到员工工种信息");
        return union;
    }

    @Override
    public PagedList<EmployeeUnion> listPagedEmployees(EmployeeWorkQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        condition.setStoreId(ThreadContext.getUserStoreId());
        List<Long> ids = employeeWorkMapper.selectEmployeeIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        List<EmployeeUnion> unions = employeeUnionMapper.selectByIds(ids);
        PageInfo<Long> info = new PageInfo<>(ids);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
