package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.EmployeeWorkTypeQueryCondition;
import com.zes.squad.gmh.entity.po.EmployeeWorkTypePo;
import com.zes.squad.gmh.entity.union.EmployeeWorkTypeUnion;
import com.zes.squad.gmh.mapper.EmployeeWorkTypeMapper;
import com.zes.squad.gmh.mapper.EmployeeWorkTypeUnionMapper;
import com.zes.squad.gmh.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeWorkTypeMapper      employeeWorkTypeMapper;
    @Autowired
    private EmployeeWorkTypeUnionMapper employeeWorkTypeUnionMapper;

    @Override
    public void createEmployeeWorkType(EmployeeWorkTypePo po) {
        po.setStoreId(ThreadContext.getUserStoreId());
        employeeWorkTypeMapper.insert(po);
    }

    @Override
    public void removeEmployeeWrokType(Long id) {
        employeeWorkTypeMapper.deleteById(id);
    }

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

}
