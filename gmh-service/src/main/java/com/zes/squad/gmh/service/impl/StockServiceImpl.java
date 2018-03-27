package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionValid;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zes.squad.gmh.common.enums.FlowTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockAmountPo;
import com.zes.squad.gmh.entity.po.StockFlowPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.mapper.StockAmountMapper;
import com.zes.squad.gmh.mapper.StockFlowMapper;
import com.zes.squad.gmh.mapper.StockMapper;
import com.zes.squad.gmh.mapper.StockTypeMapper;
import com.zes.squad.gmh.mapper.StockUnionMapper;
import com.zes.squad.gmh.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockTypeMapper   stockTypeMapper;
    @Autowired
    private StockMapper       stockMapper;
    @Autowired
    private StockUnionMapper  stockUnionMapper;
    @Autowired
    private StockAmountMapper stockAmountMapper;
    @Autowired
    private StockFlowMapper   stockFlowMapper;

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
    public List<StockTypePo> listAllStockTypes() {
        List<StockTypePo> pos = stockTypeMapper.selectAll();
        ensureCollectionNotEmpty(pos, "请先新建库存分类");
        return pos;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockPo createStock(StockPo po) {
        StockPo existingPo = stockMapper.selectByCode(po.getCode());
        ensureEntityNotExist(existingPo, "库存已存在");
        stockMapper.insert(po);
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void deleteStock(Long id) {
        int record = stockMapper.deleteById(id);
        ensureConditionValid(record == 1, "库存删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void deleteStocks(List<Long> ids) {
        int records = stockMapper.batchDelete(ids);
        ensureConditionValid(records == ids.size(), "库存删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockPo modifyStock(StockPo po) {
        StockPo stockPo = stockMapper.selectByCode(po.getCode());
        if (stockPo != null) {
            ensureConditionValid(stockPo.getId().equals(po.getId()), "库存已存在");
        }
        stockMapper.updateSelective(po);
        StockPo newPo = stockMapper.selectById(po.getId());
        return newPo;
    }

    @Override
    public StockUnion queryStockDetail(Long id) {
        StockUnion union = stockUnionMapper.selectById(id);
        ensureEntityExist(union, "库存不存在");
        return union;
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

    @Override
    public List<StockPo> listAllStocks() {
        List<StockPo> pos = stockMapper.selectAll();
        ensureCollectionNotEmpty(pos, "请先新建库存");
        return pos;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockAmountPo createStockAmount(StockAmountPo po) {
        po.setStoreId(ThreadContext.getUserStoreId());
        StockAmountPo existingPo = stockAmountMapper.selectByStockAndStore(po.getStockId(), po.getStoreId());
        ensureEntityNotExist(existingPo, "库存数量重复设置");
        int record = stockAmountMapper.insert(po);
        ensureConditionValid(record == 1, "新建库存数量失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeStockAmount(Long id) {
        int record = stockAmountMapper.deleteById(id);
        ensureConditionValid(record == 1, "库存数量删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeStockAmounts(List<Long> ids) {
        int records = stockAmountMapper.batchDelete(ids);
        ensureConditionValid(records == ids.size(), "库存数量删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockAmountPo modifyStockAmount(StockAmountPo po) {
        po.setStoreId(ThreadContext.getUserStoreId());
        int record = stockAmountMapper.updateAmount(po);
        ensureConditionValid(record == 1, "修改库存数量失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void addStockAmount(StockAmountPo po) {
        ensureParameterExist(po.getStockId(), "库存不存在");
        po.setStoreId(ThreadContext.getUserStoreId());
        int record = stockAmountMapper.addAmount(po);
        ensureConditionValid(record == 1, "库存数量修改失败");
        StockFlowPo flowPo = new StockFlowPo();
        flowPo.setStockId(po.getStockId());
        flowPo.setType(FlowTypeEnum.BUYING_IN.getKey());
        flowPo.setAmount(po.getAmount());
        flowPo.setStoreId(ThreadContext.getUserStoreId());
        record = stockFlowMapper.insert(flowPo);
        ensureConditionValid(record == 1, "库存流水生成失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void reduceStockAmount(StockAmountPo po) {
        ensureParameterExist(po.getStockId(), "库存不存在");
        po.setStoreId(ThreadContext.getUserStoreId());
        int record = stockAmountMapper.reduceAmount(po);
        ensureConditionValid(record == 1, "库存数量修改失败");
        StockAmountPo newPo = stockAmountMapper.selectById(po.getId());
        ensureConditionValid(
                newPo.getAmount().compareTo(BigDecimal.ZERO) == 0 || newPo.getAmount().compareTo(BigDecimal.ZERO) == 1,
                "库存余量不足,请及时补充");
        StockFlowPo flowPo = new StockFlowPo();
        flowPo.setStockId(po.getStockId());
        flowPo.setType(FlowTypeEnum.SELLING_OUT.getKey());
        flowPo.setAmount(po.getAmount());
        flowPo.setStoreId(ThreadContext.getUserStoreId());
        record = stockFlowMapper.insert(flowPo);
        ensureConditionValid(record == 1, "库存流水生成失败");
    }

    @Override
    public StockUnion queryStockDetailWithAmount(Long id) {
        StockUnion union = stockUnionMapper.selectWithAmountById(id);
        ensureEntityExist(union, "库存不存在");
        ensureEntityExist(union.getStockPo(), "库存不存在");
        ensureEntityExist(union.getStockTypePo(), "库存分类不存在");
        ensureEntityExist(union.getStockAmountPo(), "库存数量不存在");
        return union;
    }

    @Override
    public PagedList<StockUnion> listPagedStocksWithAmount(StockQueryCondition condition) {
        condition.setStoreId(ThreadContext.getUserStoreId());
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<StockUnion> unions = stockUnionMapper.selectWithAmountByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StockUnion> info = new PageInfo<>(unions);
        return PagedLists.newPagedList(info.getPageNum(), info.getPageSize(), info.getTotal(), unions);
    }

}
