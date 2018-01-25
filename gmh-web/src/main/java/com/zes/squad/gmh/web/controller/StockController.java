package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.*;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockTypeUnion;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.service.StockService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.param.stock.StockCreateOrModifyParams;
import com.zes.squad.gmh.web.param.stock.StockDeleteParams;
import com.zes.squad.gmh.web.param.stock.StockQueryParams;
import com.zes.squad.gmh.web.param.stock.StockTypeCreateOrModifyParams;
import com.zes.squad.gmh.web.param.stock.StockTypeDeleteParams;
import com.zes.squad.gmh.web.param.stock.StockTypeQueryParams;
import com.zes.squad.gmh.web.vo.StockTypeVo;
import com.zes.squad.gmh.web.vo.StockVo;

@RequestMapping(path = "/stock")
@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(path = "/type/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateStockType(@RequestBody StockTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "库存分类为空");
        ensureParameterExist(params.getName(), "库存分类名称为空");
        ensureParameterNotExist(params.getId(), "库存分类标识应为空");
        StockTypePo po = CommonConverter.map(params, StockTypePo.class);
        stockService.createStockType(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/{id}", method = { RequestMethod.DELETE })
    public JsonResult<Void> doDeleteStockType(@PathVariable("id") Long id) {
        ensureParameterExist(id, "库存分类标识为空");
        stockService.deleteStockType(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/remove", method = { RequestMethod.DELETE })
    public JsonResult<Void> doDeleteStockTypes(@RequestBody StockTypeDeleteParams params) {
        ensureParameterExist(params, "库存分类标识为空");
        ensureCollectionNotEmpty(params.getIds(), "库存分类标识为空");
        stockService.deleteStockTypes(params.getIds());
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/modify")
    public JsonResult<Void> doModifyStockType(@RequestBody StockTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "库存分类为空");
        ensureParameterExist(params.getId(), "库存分类标识为空");
        ensureParameterExist(params.getName(), "库存分类名称为空");
        StockTypePo po = CommonConverter.map(params, StockTypePo.class);
        stockService.modifyStockType(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/type/{id}", method = { RequestMethod.GET })
    public JsonResult<StockTypeVo> doQueryStockType(@PathVariable("id") Long id) {
        ensureParameterExist(id, "库存分类标识为空");
        StockTypeUnion union = stockService.queryStockTypeDetail(id);
        StockTypeVo vo = CommonConverter.map(union.getStockTypePo(), StockTypeVo.class);
        vo.setStoreName(union.getStoreName());
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/type/list", method = { RequestMethod.GET })
    public JsonResult<PagedList<StockTypeVo>> doQueryStockTypes(@RequestBody StockTypeQueryParams params) {
        ensureParameterExist(params, "库存分类查询条件为空");
        StockTypeQueryCondition condition = CommonConverter.map(params, StockTypeQueryCondition.class);
        PagedList<StockTypeUnion> pagedUnions = stockService.queryStockTypesByCondition(condition);
        if (pagedUnions == null || CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<StockTypeVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (StockTypeUnion union : pagedUnions.getData()) {
            StockTypeVo vo = CommonConverter.map(union.getStockTypePo(), StockTypeVo.class);
            vo.setStoreName(union.getStoreName());
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/create", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateStock(@RequestBody StockCreateOrModifyParams params) {
        ensureParameterExist(params, "库存信息为空");
        ensureParameterNotExist(params.getId(), "库存标识应为空");
        ensureParameterExist(params.getStockTypeId(), "库存分类为空");
        ensureParameterExist(params.getName(), "库存名称为空");
        ensureParameterExist(params.getTotalAmount(), "库存余量为空");
        ensureParameterValid(params.getTotalAmount().compareTo(BigDecimal.ZERO) == 1, "库存余量错误");
        StockPo po = CommonConverter.map(params, StockPo.class);
        stockService.createStock(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveStock(@PathVariable("id") Long id) {
        ensureParameterExist(id, "库存标识为空");
        stockService.deleteStock(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/remove", method = { RequestMethod.DELETE })
    public JsonResult<Void> doRemoveStocks(@RequestBody StockDeleteParams params) {
        ensureParameterExist(params, "请选择要删除的库存");
        ensureCollectionNotEmpty(params.getIds(), "请选择要删除的库存");
        stockService.deleteStocks(params.getIds());
        return JsonResults.success();
    }

    @RequestMapping(path = "/modify", method = { RequestMethod.POST })
    public JsonResult<Void> doModifyStock(@RequestBody StockCreateOrModifyParams params) {
        ensureParameterExist(params, "库存信息为空");
        ensureParameterExist(params.getId(), "库存标识为空");
        if (params.getTotalAmount() != null) {
            ensureParameterValid(params.getTotalAmount().compareTo(BigDecimal.ZERO) == 1, "库存余量错误");
        }
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<StockVo> doQueryStockTypeById(@PathVariable("id") Long id) {
        ensureParameterExist(id, "库存标识为空");
        StockUnion union = stockService.queryStockById(id);
        StockVo vo = CommonConverter.map(union.getStockPo(), StockVo.class);
        vo.setStockTypeName(union.getStockTypePo().getName());
        vo.setStoreName(union.getStoreName());
        return JsonResults.success();
    }

    @RequestMapping(path = "/list", method = { RequestMethod.GET })
    public JsonResult<PagedList<StockVo>> doListPagedStocks(@RequestBody StockQueryParams params) {
        ensureParameterExist(params, "库存查询条件为空");
        int pageNum = params.getPageNum();
        int pageSize = params.getPageSize();
        ensureParameterExist(pageNum, "分页页码为空");
        ensureParameterValid(pageNum > 0, "分页页码为空");
        ensureParameterExist(pageSize, "分页大小为空");
        ensureParameterValid(pageSize > 0, "分页大小为空");
        StockQueryCondition condition = CommonConverter.map(params, StockQueryCondition.class);
        PagedList<StockUnion> pagedUnions = stockService.listPagedStocks(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success();
        }
        List<StockVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (StockUnion union : pagedUnions.getData()) {
            StockVo vo = CommonConverter.map(union.getStockPo(), StockVo.class);
            vo.setStockTypeName(union.getStockTypePo().getName());
            vo.setStoreName(union.getStoreName());
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/statistics", method = { RequestMethod.GET })
    public JsonResult<PagedList<StockVo>> doStatStocks() {
        return JsonResults.success();
    }

}
