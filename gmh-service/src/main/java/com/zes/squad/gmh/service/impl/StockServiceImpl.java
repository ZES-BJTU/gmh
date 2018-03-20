package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionValid;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.mapper.StockMapper;
import com.zes.squad.gmh.mapper.StockTypeMapper;
import com.zes.squad.gmh.mapper.StockUnionMapper;
import com.zes.squad.gmh.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockTypeMapper  stockTypeMapper;
    @Autowired
    private StockMapper      stockMapper;
    @Autowired
    private StockUnionMapper stockUnionMapper;

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockTypePo createStockType(StockTypePo po) {
        StockTypePo existingPo = stockTypeMapper.selectByName(po.getName());
        ensureEntityNotExist(existingPo, "库存分类已存在");
        stockTypeMapper.insert(po);
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void deleteStockType(Long id) {
        int row = stockTypeMapper.deleteById(id);
        ensureConditionValid(row == 1, "库存分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void deleteStockTypes(List<Long> ids) {
        int rows = stockTypeMapper.batchDelete(ids);
        ensureConditionValid(rows == ids.size(), "库存分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockTypePo modifyStockType(StockTypePo po) {
        StockTypePo typePo = stockTypeMapper.selectByName(po.getName());
        if (typePo != null) {
            ensureConditionValid(typePo.getId().equals(po.getId()), "库存分类已存在");
        }
        stockTypeMapper.updateSelective(po);
        StockTypePo newTypePo = stockTypeMapper.selectById(po.getId());
        ensureEntityExist(newTypePo, "库存分类不存在");
        return newTypePo;
    }

    @Override
    public StockTypePo queryStockTypeDetail(Long id) {
        StockTypePo po = stockTypeMapper.selectById(id);
        ensureEntityExist(po, "库存分类获取失败");
        return po;
    }

    @Override
    public PagedList<StockTypePo> listPagedStockTypes(StockTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<StockTypePo> pos = stockTypeMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(pos)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StockTypePo> pageInfo = new PageInfo<>(pos);
        return PagedLists.newPagedList(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), pos);
    }

    @Override
    public void createStock(StockPo po) {
        stockMapper.insert(po);
    }

    @Override
    public void deleteStock(Long id) {
        stockMapper.deleteById(id);
    }

    @Override
    public void deleteStocks(List<Long> ids) {
        stockMapper.batchDelete(ids);
    }

    @Override
    public void modifyStock(StockPo po) {
        stockMapper.updateSelective(po);
    }

    @Override
    public StockUnion queryStock(Long id) {
        return stockUnionMapper.selectById(id);
    }

    @Override
    public PagedList<StockUnion> listPagedStocks(StockQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        condition.setStoreId(ThreadContext.getUserStoreId());
        List<StockUnion> unions = stockUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StockUnion> info = new PageInfo<StockUnion>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
