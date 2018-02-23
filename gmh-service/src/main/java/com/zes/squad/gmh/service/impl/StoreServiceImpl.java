package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StoreQueryCondition;
import com.zes.squad.gmh.entity.po.StorePo;
import com.zes.squad.gmh.entity.union.StoreUnion;
import com.zes.squad.gmh.mapper.StoreMapper;
import com.zes.squad.gmh.mapper.StoreUnionMapper;
import com.zes.squad.gmh.service.StoreService;

@Service("storeService")
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper      storeMapper;
    @Autowired
    private StoreUnionMapper storeUnionMapper;

    @Override
    public void createStore(StorePo po) {
        storeMapper.insert(po);
    }

    @Override
    public void removeStore(Long id) {
        storeMapper.deleteById(id);
    }

    @Override
    public void removeStores(List<Long> ids) {
        storeMapper.batchDelete(ids);
    }

    @Override
    public void modifyStore(StorePo po) {
        storeMapper.updateSelective(po);
    }

    @Override
    public StoreUnion queryStoreDetail(Long id) {
        StoreUnion union = storeUnionMapper.selectById(id);
        ensureEntityExist(union, "获取门店详情失败");
        ensureEntityExist(union.getStorePo(), "获取门店信息失败");
        ensureEntityExist(union.getUserPo(), "获取门店负责人信息失败");
        return union;
    }

    @Override
    public PagedList<StoreUnion> listStoresByPage(StoreQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<StoreUnion> unions = storeUnionMapper.selectByCondition(condition);
        if (unions.isEmpty()) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StoreUnion> pageInfo = new PageInfo<>(unions);
        return PagedLists.newPagedList(pageNum, pageSize, pageInfo.getTotal(), unions);
    }

}
