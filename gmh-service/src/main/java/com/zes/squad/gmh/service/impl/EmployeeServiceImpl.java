package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;
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
    public EmployeeUnion createEmployee(EmployeeUnion union) {
        EmployeePo existingPo = employeeMapper.selectByMobile(union.getEmployeePo().getMobile());
        ensureEntityNotExist(existingPo, "手机号已被占用");
        EmployeePo employeePo = union.getEmployeePo();
        if (employeePo.getEntryTime() == null) {
            employeePo.setEntryTime(new Date());
        }
        employeePo.setWorking(true);
        employeePo.setStoreId(ThreadContext.getUserStoreId());
        int record = employeeMapper.insert(employeePo);
        ensureConditionSatisfied(record == 1, "添加员工失败");
        ensureParameterExist(employeePo.getId(), "添加员工失败");
        List<EmployeeWorkPo> workPos = Lists.newArrayListWithCapacity(union.getEmployeeWorkPos().size());
        for (EmployeeWorkPo workPo : union.getEmployeeWorkPos()) {
            workPo.setEmployeeId(employeePo.getId());
            workPos.add(workPo);
        }
        int records = employeeWorkMapper.batchInsert(workPos);
        ensureConditionSatisfied(records == union.getEmployeeWorkPos().size(), "设置员工工种失败");
        List<EmployeeWorkPo> employeeWorkPos = employeeWorkMapper.selectByEmployeeId(employeePo.getId());
        union.setEmployeeWorkPos(employeeWorkPos);
        return union;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeEmployee(Long id) {
        int record = employeeMapper.updateWorkingById(id);
        ensureConditionSatisfied(record == 1, "删除员工失败");
        employeeWorkMapper.batchDeleteByEmployeeId(id);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeEmployees(List<Long> ids) {
        int records = employeeMapper.batchUpdateWorkingByIds(ids);
        ensureConditionSatisfied(records == ids.size(), "删除员工失败");
        employeeWorkMapper.batchDeleteByEmployeeIds(ids);
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public EmployeeUnion modifyEmployee(EmployeeUnion union) {
        EmployeePo existingPo = employeeMapper.selectByMobile(union.getEmployeePo().getMobile());
        if (existingPo != null) {
            ensureConditionSatisfied(existingPo.getId().equals(union.getEmployeePo().getId()), "手机号已被占用");
        }
        EmployeePo employeePo = union.getEmployeePo();
        int record = employeeMapper.updateSelective(employeePo);
        ensureConditionSatisfied(record == 1, "员工信息修改失败");
        employeePo = employeeMapper.selectById(employeePo.getId());
        Long employeeId = employeePo.getId();
        employeeWorkMapper.batchDeleteByEmployeeId(employeeId);
        if (employeePo.getWorking().booleanValue()) {
            List<EmployeeWorkPo> workPos = Lists.newArrayListWithCapacity(union.getEmployeeWorkPos().size());
            for (EmployeeWorkPo workPo : union.getEmployeeWorkPos()) {
                workPo.setEmployeeId(employeePo.getId());
                workPos.add(workPo);
            }
            int records = employeeWorkMapper.batchInsert(workPos);
            ensureConditionSatisfied(records == workPos.size(), "员工工种信息修改失败");
            List<EmployeeWorkPo> employeeWorkPos = employeeWorkMapper.selectByEmployeeId(employeePo.getId());
            union.setEmployeeWorkPos(employeeWorkPos);
        } else {
            union.setEmployeeWorkPos(null);
        }
        return union;
    }

    @Override
    public EmployeeUnion queryEmployeeDetail(Long id) {
        EmployeeUnion union = employeeUnionMapper.selectById(id);
        ensureEntityExist(union, "未找到员工详细信息");
        ensureEntityExist(union.getEmployeePo(), "未找到员工信息");
        ensureCollectionNotEmpty(union.getEmployeeWorkPos(), "未找到员工工种信息");
        return union;
    }

    @Override
    public PagedList<EmployeeUnion> listPagedEmployees(EmployeeWorkQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        condition.setStoreId(ThreadContext.getUserStoreId());
        List<Long> ids = employeeMapper.selectEmployeeIdsByCondition(condition);
        if (CollectionUtils.isEmpty(ids)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        List<EmployeeUnion> unions = employeeUnionMapper.selectByIds(ids);
        PageInfo<Long> info = new PageInfo<>(ids);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

    @Override
    public List<EmployeePo> listEmployeesByWorkType(Integer workType) {
        return employeeMapper.selectByWorkType(workType, ThreadContext.getUserStoreId());
    }

}
