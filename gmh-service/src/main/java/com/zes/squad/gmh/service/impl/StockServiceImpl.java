package com.zes.squad.gmh.service.impl;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureConditionSatisfied;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureEntityNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.helper.ExcelHelper.generateNumericCell;
import static com.zes.squad.gmh.helper.ExcelHelper.generateStringCell;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.enums.FlowTypeEnum;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.common.util.DateUtils;
import com.zes.squad.gmh.common.util.EnumUtils;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockAmountPo;
import com.zes.squad.gmh.entity.po.StockFlowPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.po.StorePo;
import com.zes.squad.gmh.entity.union.StockFlowUnion;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.mapper.StockAmountMapper;
import com.zes.squad.gmh.mapper.StockFlowMapper;
import com.zes.squad.gmh.mapper.StockFlowUnionMapper;
import com.zes.squad.gmh.mapper.StockMapper;
import com.zes.squad.gmh.mapper.StockTypeMapper;
import com.zes.squad.gmh.mapper.StockUnionMapper;
import com.zes.squad.gmh.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService {

    @Autowired
    private StockTypeMapper      stockTypeMapper;
    @Autowired
    private StockMapper          stockMapper;
    @Autowired
    private StockUnionMapper     stockUnionMapper;
    @Autowired
    private StockAmountMapper    stockAmountMapper;
    @Autowired
    private StockFlowMapper      stockFlowMapper;
    @Autowired
    private StockFlowUnionMapper stockFlowUnionMapper;

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
        List<StockPo> pos = stockMapper.selectByTypeId(id);
        ensureCollectionEmpty(pos, "库存分类已被使用,无法删除");
        int row = stockTypeMapper.deleteById(id);
        ensureConditionSatisfied(row == 1, "库存分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void deleteStockTypes(List<Long> ids) {
        int rows = stockTypeMapper.batchDelete(ids);
        ensureConditionSatisfied(rows == ids.size(), "库存分类删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockTypePo modifyStockType(StockTypePo po) {
        StockTypePo typePo = stockTypeMapper.selectByName(po.getName());
        if (typePo != null) {
            ensureConditionSatisfied(typePo.getId().equals(po.getId()), "库存分类已存在");
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
        ensureEntityNotExist(existingPo, "库存代码已被占用");
        int record = stockMapper.insert(po);
        ensureConditionSatisfied(record == 1, "库存新建成功");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void deleteStock(Long id) {
        List<StockAmountPo> pos = stockAmountMapper.selectByStockId(id);
        ensureCollectionEmpty(pos, "库存已被使用,无法删除");
        int record = stockMapper.deleteById(id);
        ensureConditionSatisfied(record == 1, "库存删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void deleteStocks(List<Long> ids) {
        int records = stockMapper.batchDelete(ids);
        ensureConditionSatisfied(records == ids.size(), "库存删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockPo modifyStock(StockPo po) {
        StockPo stockPo = stockMapper.selectByCode(po.getCode());
        if (stockPo != null) {
            ensureConditionSatisfied(stockPo.getId().equals(po.getId()), "库存代码已被占用");
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
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何店铺");
        po.setStoreId(storeId);
        StockAmountPo existingPo = stockAmountMapper.selectByStockAndStore(po.getStockId(), po.getStoreId());
        ensureEntityNotExist(existingPo, "库存数量重复设置");
        int record = stockAmountMapper.insert(po);
        ensureConditionSatisfied(record == 1, "新建库存数量失败");
        StockFlowPo flowPo = new StockFlowPo();
        flowPo.setStockId(po.getStockId());
        flowPo.setType(FlowTypeEnum.FIRST_BUYING_IN.getKey());
        flowPo.setAmount(po.getAmount());
        flowPo.setStoreId(storeId);
        record = stockFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "库存流水生成失败");
        return po;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeStockAmount(Long id) {
        int record = stockAmountMapper.deleteById(id);
        ensureConditionSatisfied(record == 1, "库存数量删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void removeStockAmounts(List<Long> ids) {
        int records = stockAmountMapper.batchDelete(ids);
        ensureConditionSatisfied(records == ids.size(), "库存数量删除失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockAmountPo modifyStockAmount(StockAmountPo po) {
        int record = stockAmountMapper.updateAmount(po);
        ensureConditionSatisfied(record == 1, "库存数量修改失败");
        StockAmountPo newPo = stockAmountMapper.selectById(po.getId());
        ensureEntityExist(newPo, "库存数量不存在");
        StockFlowPo flowPo = new StockFlowPo();
        flowPo.setStockId(newPo.getStockId());
        flowPo.setType(FlowTypeEnum.ADJUSTMENT.getKey());
        flowPo.setAmount(newPo.getAmount());
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何店铺");
        flowPo.setStoreId(storeId);
        record = stockFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "库存流水生成失败");
        return newPo;
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public StockAmountPo addStockAmount(StockAmountPo po) {
        StockAmountPo amountPo = stockAmountMapper.selectById(po.getId());
        ensureEntityExist(amountPo, "库存不存在");
        Long storeId = ThreadContext.getUserStoreId();
        ensureParameterExist(storeId, "当前用户不属于任何店铺");
        po.setStoreId(storeId);
        po.setStockId(amountPo.getStockId());
        int record = stockAmountMapper.addAmount(po);
        ensureConditionSatisfied(record == 1, "库存数量修改失败");
        StockFlowPo flowPo = new StockFlowPo();
        flowPo.setStockId(po.getStockId());
        flowPo.setType(FlowTypeEnum.BUYING_IN.getKey());
        flowPo.setAmount(po.getAmount());
        flowPo.setStoreId(storeId);
        record = stockFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "库存流水生成失败");
        return stockAmountMapper.selectById(po.getId());
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void reduceStockAmount(StockFlowPo flowPo) {
        ensureParameterExist(flowPo.getStockId(), "库存不存在");
        ensureParameterExist(flowPo.getRecordId(), "消费记录id为空");
        ensureParameterExist(flowPo.getAmount(), "库存消耗为空");
        Long storeId = ThreadContext.getUserStoreId();
        ensureParameterExist(storeId, "当前用户不属于任何店铺");
        flowPo.setStoreId(storeId);
        flowPo.setType(FlowTypeEnum.SELLING_OUT.getKey());
        int record = stockFlowMapper.insert(flowPo);
        ensureConditionSatisfied(record == 1, "库存流水生成失败");
        StockAmountPo amountPo = stockAmountMapper.selectByStockAndStore(flowPo.getStockId(), storeId);
        ensureEntityExist(amountPo, "无对应库存");
        ensureConditionSatisfied(amountPo.getAmount().compareTo(flowPo.getAmount()) == 0
                || amountPo.getAmount().compareTo(flowPo.getAmount()) == 1, "库存余量不足,请及时补充");
        StockAmountPo po = new StockAmountPo();
        po.setId(amountPo.getId());
        po.setAmount(flowPo.getAmount());
        record = stockAmountMapper.reduceAmount(po);
        ensureConditionSatisfied(record == 1, "库存数量修改失败");
    }

    @Transactional(rollbackFor = { Throwable.class })
    @Override
    public void modifyFlowInvalid(Long recordId) {
        ensureParameterExist(recordId, "消费记录不存在");
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何店铺");
        List<StockFlowPo> pos = stockFlowMapper.selectByRecordId(recordId, storeId);
        ensureCollectionNotEmpty(pos, "消费记录没有库存消耗记录");
        List<StockAmountPo> amountPos = Lists.newArrayListWithCapacity(pos.size());
        for (StockFlowPo po : pos) {
            StockAmountPo amountPo = new StockAmountPo();
            amountPo.setStockId(po.getStockId());
            amountPo.setStoreId(po.getStoreId());
            amountPo.setAmount(po.getAmount());
            amountPos.add(amountPo);
        }
        int records = stockAmountMapper.batchAddAmountByStockAndStore(amountPos);
        ensureConditionSatisfied(records == amountPos.size(), "库存数量修改失败");
        int record = stockFlowMapper.updateStatusByRecordId(recordId);
        ensureConditionSatisfied(record == 1, "库存流水置为无效失败");
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

    @Override
    public Workbook exportStocks(Date beginTime, Date endTime) {
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("库存流水统计");
        Long storeId = ThreadContext.getUserStoreId();
        ensureEntityExist(storeId, "当前用户不属于任何门店");
        List<StockFlowUnion> unions = stockFlowUnionMapper.selectAll(storeId, beginTime, endTime);
        if (CollectionUtils.isEmpty(unions)) {
            return workbook;
        }
        buildSheetByStockFlowUnions(sheet, unions);
        return workbook;
    }

    private void buildSheetByStockFlowUnions(Sheet sheet, List<StockFlowUnion> unions) {
        int rowNum = 0;
        int columnNum = 0;
        Row row = sheet.createRow(rowNum++);
        generateStringCell(row, columnNum++, "门店名称");
        generateStringCell(row, columnNum++, "库存分类名称");
        generateStringCell(row, columnNum++, "库存代码");
        generateStringCell(row, columnNum++, "库存名称");
        generateStringCell(row, columnNum++, "库存计量单位");
        generateStringCell(row, columnNum++, "库存流水分类");
        generateStringCell(row, columnNum++, "库存流水数量");
        generateStringCell(row, columnNum++, "库存流水状态");
        generateStringCell(row, columnNum++, "库存流水时间");
        for (StockFlowUnion union : unions) {
            StockTypePo typePo = union.getStockTypePo();
            ensureEntityExist(typePo, "库存分类不存在");
            StockPo po = union.getStockPo();
            ensureEntityExist(po, "库存不存在");
            StockFlowPo flowPo = union.getStockFlowPo();
            ensureEntityExist(po, "库存流水不存在");
            StorePo storePo = union.getStorePo();
            ensureEntityExist(po, "库存流水所属门店不存在");
            columnNum = 0;
            row = sheet.createRow(rowNum++);
            //门店名称
            generateStringCell(row, columnNum++, storePo.getName());
            //库存分类名称
            generateStringCell(row, columnNum++, typePo.getName());
            //库存代码
            generateStringCell(row, columnNum++, po.getCode());
            //库存名称
            generateStringCell(row, columnNum++, po.getName());
            //库存计量单位
            generateStringCell(row, columnNum++, po.getUnitName());
            //库存流水分类
            generateStringCell(row, columnNum++, EnumUtils.getDescByKey(flowPo.getType(), FlowTypeEnum.class));
            //库存流水数量
            generateNumericCell(row, columnNum++, flowPo.getAmount().doubleValue());
            //库存流水状态
            generateStringCell(row, columnNum++, getStatusDesc(flowPo.getStatus()));
            //库存流水时间
            generateStringCell(row, columnNum++, DateUtils.formatDateTime(flowPo.getCreatedTime()));
        }
    }

    private String getStatusDesc(int status) {
        if (status == 1) {
            return "有效";
        }
        if (status == 1) {
            return "无效";
        }
        return "错误";
    }

}
