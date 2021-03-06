package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StoreQueryCondition;
import com.zes.squad.gmh.entity.po.StorePo;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.union.StoreUnion;
import com.zes.squad.gmh.mapper.StoreMapper;
import com.zes.squad.gmh.mapper.StoreUnionMapper;
import com.zes.squad.gmh.mapper.UserMapper;
import com.zes.squad.gmh.service.StoreService;

@Service("storeService")
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper      storeMapper;
    @Autowired
    private StoreUnionMapper storeUnionMapper;
    @Autowired
    private UserMapper       userMapper;

    @Override
    public StorePo createStore(StorePo po) {
        StorePo existingPo = storeMapper.selectByName(po.getName());
        ensureEntityNotExist(existingPo, "门店名称被占用");
        storeMapper.insert(po);
        return po;
    }

    @Override
    public void removeStore(Long id) {
        List<UserPo> pos = userMapper.selectByStoreId(id);
        ensureCollectionEmpty(pos, "该门店下存在用户,无法删除");
        storeMapper.deleteById(id);
    }

    @Override
    public void removeStores(List<Long> ids) {
        storeMapper.batchDelete(ids);
    }

    @Override
    public StorePo modifyStore(StorePo po) {
        if (!Strings.isNullOrEmpty(po.getName())) {
            StorePo storePo = storeMapper.selectByName(po.getName());
            if (storePo != null) {
                ensureConditionSatisfied(storePo.getId().equals(po.getId()), "门店名称被占用");
            }
        }
        storeMapper.updateSelective(po);
        StorePo newStorePo = storeMapper.selectById(po.getId());
        ensureEntityExist(newStorePo, "该门店不存在");
        return newStorePo;
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
    public PagedList<StoreUnion> listPagedStores(StoreQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<StoreUnion> unions = storeUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StoreUnion> pageInfo = new PageInfo<>(unions);
        return PagedLists.newPagedList(pageNum, pageSize, pageInfo.getTotal(), unions);
    }

    @Override
    public List<StorePo> listStores() {
        return storeMapper.selectAll();
    }

}
