package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockTypeUnion;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.mapper.StockMapper;
import com.zes.squad.gmh.mapper.StockTypeMapper;
import com.zes.squad.gmh.mapper.StockTypeUnionMapper;
import com.zes.squad.gmh.mapper.StockUnionMapper;
import com.zes.squad.gmh.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockTypeMapper      stockTypeMapper;
    @Autowired
    private StockTypeUnionMapper stockTypeUnionMapper;
    @Autowired
    private StockMapper          stockMapper;
    @Autowired
    private StockUnionMapper     stockUnionMapper;

    @Override
    public void createStockType(StockTypePo po) {
        po.setStoreId(ThreadContext.getUserStoreId());
        stockTypeMapper.insert(po);
    }

    @Override
    public void deleteStockType(Long id) {
        stockTypeMapper.deleteById(id);
    }

    @Override
    public void deleteStockTypes(List<Long> ids) {
        stockTypeMapper.batchDelete(ids);
    }

    @Override
    public void modifyStockType(StockTypePo po) {
        stockTypeMapper.updateSelective(po);
    }

    @Override
    public StockTypeUnion queryStockTypeDetail(Long id) {
        StockTypeUnion union = stockTypeUnionMapper.selectById(id);
        ensureEntityExist(union, "库存分类查询失败");
        return union;
    }

    @Override
    public PagedList<StockTypeUnion> queryStockTypesByCondition(StockTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<StockTypeUnion> unions = stockTypeUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StockTypeUnion> pageInfo = new PageInfo<>(unions);
        return PagedLists.newPagedList(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), unions);
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
    public StockUnion queryStockById(Long id) {
        return stockUnionMapper.selectById(id);
    }

    @Override
    public PagedList<StockUnion> listPagedStocks(StockQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<StockUnion> unions = stockUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StockUnion> info = new PageInfo<StockUnion>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
