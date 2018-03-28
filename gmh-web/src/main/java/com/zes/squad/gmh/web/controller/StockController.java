package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StockQueryCondition;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockAmountPo;
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.service.StockService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.StockAmountParams;
import com.zes.squad.gmh.web.entity.param.StockParams;
import com.zes.squad.gmh.web.entity.param.StockQueryParams;
import com.zes.squad.gmh.web.entity.param.StockTypeParams;
import com.zes.squad.gmh.web.entity.param.StockTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.StockAmountVo;
import com.zes.squad.gmh.web.entity.vo.StockTypeVo;
import com.zes.squad.gmh.web.entity.vo.StockVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/stocks")
@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(path = "/types", method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<StockTypeVo> doCreateStockType(@RequestBody StockTypeParams params) {
        ensureParameterExist(params, "库存分类为空");
        ensureParameterExist(params.getName(), "库存分类名称为空");
        ensureParameterNotExist(params.getId(), "库存分类已存在");
        StockTypePo po = CommonConverter.map(params, StockTypePo.class);
        StockTypePo newTypePo = stockService.createStockType(po);
        StockTypeVo vo = CommonConverter.map(newTypePo, StockTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doDeleteStockType(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除库存分类");
        stockService.deleteStockType(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/types", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doDeleteStockTypes(@RequestBody List<Long> ids) {
        ensureParameterExist(ids, "请选择待删除库存分类");
        ensureCollectionNotEmpty(ids, "请选择待删除库存分类");
        stockService.deleteStockTypes(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.PUT })
    public JsonResult<StockTypeVo> doModifyStockType(@PathVariable("id") Long id, @RequestBody StockTypeParams params) {
        ensureParameterExist(params, "请选择待修改库存分类");
        ensureParameterExist(id, "请选择待修改库存分类");
        params.setId(id);
        ensureParameterExist(params.getName(), "库存分类名称为空");
        StockTypePo po = CommonConverter.map(params, StockTypePo.class);
        StockTypePo newTypePo = stockService.modifyStockType(po);
        StockTypeVo vo = CommonConverter.map(newTypePo, StockTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.GET })
    public JsonResult<StockTypeVo> doQueryStockTypeDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "库存分类标识为空");
        StockTypePo po = stockService.queryStockTypeDetail(id);
        StockTypeVo vo = CommonConverter.map(po, StockTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types", method = { RequestMethod.GET })
    public JsonResult<PagedList<StockTypeVo>> doListPagedStockTypes(StockTypeQueryParams queryParams) {
        ensureParameterExist(queryParams, "库存分类查询条件为空");
        CheckHelper.checkPageParams(queryParams);
        StockTypeQueryCondition condition = CommonConverter.map(queryParams, StockTypeQueryCondition.class);
        PagedList<StockTypePo> pagedPos = stockService.listPagedStockTypes(condition);
        if (CollectionUtils.isEmpty(pagedPos.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedPos.getPageNum(), pagedPos.getPageSize()));
        }
        PagedList<StockTypeVo> pagedVos = CommonConverter.mapPagedList(pagedPos, StockTypeVo.class);
        return JsonResults.success(pagedVos);
    }

    @RequestMapping(path = "/types/all", method = { RequestMethod.GET })
    public JsonResult<List<StockTypeVo>> doListAllStockTypes() {
        List<StockTypePo> pos = stockService.listAllStockTypes();
        List<StockTypeVo> vos = CommonConverter.mapList(pos, StockTypeVo.class);
        return JsonResults.success(vos);
    }

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<StockVo> doCreateStock(@RequestBody StockParams params) {
        ensureParameterExist(params, "库存信息为空");
        ensureParameterNotExist(params.getId(), "库存已存在");
        ensureParameterExist(params.getStockTypeId(), "库存分类为空");
        ensureParameterExist(params.getCode(), "库存代码为空");
        ensureParameterExist(params.getName(), "库存名称为空");
        ensureParameterExist(params.getUnitName(), "库存计量单位为空");
        StockPo po = CommonConverter.map(params, StockPo.class);
        StockPo newPo = stockService.createStock(po);
        StockVo vo = CommonConverter.map(newPo, StockVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveStock(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择要删除的库存");
        stockService.deleteStock(id);
        return JsonResults.success();
    }

    @RequestMapping(method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveStocks(@RequestBody List<Long> ids) {
        ensureParameterExist(ids, "请选择要删除的库存");
        ensureCollectionNotEmpty(ids, "请选择要删除的库存");
        stockService.deleteStocks(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<StockVo> doModifyStock(@PathVariable("id") Long id, @RequestBody StockParams params) {
        ensureParameterExist(id, "库存标识为空");
        ensureParameterExist(params, "库存信息为空");
        params.setId(id);
        StockPo po = CommonConverter.map(params, StockPo.class);
        StockPo newPo = stockService.modifyStock(po);
        StockVo vo = CommonConverter.map(newPo, StockVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<StockVo> doQueryStockDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "库存标识为空");
        StockUnion union = stockService.queryStockDetail(id);
        StockVo vo = buildStockVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<StockVo>> doListPagedStocks(StockQueryParams params) {
        CheckHelper.checkPageParams(params);
        StockQueryCondition condition = CommonConverter.map(params, StockQueryCondition.class);
        PagedList<StockUnion> pagedUnions = stockService.listPagedStocks(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<StockVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (StockUnion union : pagedUnions.getData()) {
            StockVo vo = buildStockVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/all", method = { RequestMethod.GET })
    public JsonResult<List<StockVo>> doListAllStocks() {
        List<StockPo> pos = stockService.listAllStocks();
        List<StockVo> vos = CommonConverter.mapList(pos, StockVo.class);
        return JsonResults.success(vos);
    }

    @RequestMapping(path = "/amount", method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<StockAmountVo> doCreateStockAmount(@RequestBody StockAmountParams params) {
        ensureParameterExist(params, "请输入库存数量");
        ensureParameterNotExist(params.getId(), "库存数量已存在");
        ensureParameterExist(params.getStockId(), "请选择库存");
        ensureParameterExist(params.getAmount(), "请输入库存数量");
        ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "库存数量应大于0");
        StockAmountPo po = CommonConverter.map(params, StockAmountPo.class);
        StockAmountPo newPo = stockService.createStockAmount(po);
        StockAmountVo vo = CommonConverter.map(newPo, StockAmountVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveStockAmount(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除库存数量");
        stockService.removeStockAmount(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/amount", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<StockAmountVo> createStockAmount(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除库存数量");
        stockService.removeStockAmounts(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.PUT })
    public JsonResult<StockAmountVo> doModifyStockAmount(@PathVariable("id") Long id,
                                                         @RequestBody StockAmountParams params) {
        ensureParameterExist(id, "请选择待修改库存数量");
        ensureParameterExist(params, "请输入待修改库存数量");
        ensureParameterExist(params.getStockId(), "请选择待修改库存");
        params.setId(id);
        if (params.getAmount() != null) {
            ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "库存数量应大于0");
        }
        StockAmountPo po = CommonConverter.map(params, StockAmountPo.class);
        StockAmountPo newPo = stockService.modifyStockAmount(po);
        StockAmountVo vo = CommonConverter.map(newPo, StockAmountVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.PATCH })
    public JsonResult<StockAmountVo> doAddStockAmount(@PathVariable("id") Long id,
                                                      @RequestBody StockAmountParams params) {
        ensureParameterExist(id, "请选择待补充库存数量");
        ensureParameterExist(params, "请输入待补充库存数量");
        params.setId(id);
        if (params.getAmount() != null) {
            ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "库存数量应大于0");
        }
        StockAmountPo po = CommonConverter.map(params, StockAmountPo.class);
        StockAmountPo newPo = stockService.addStockAmount(po);
        StockAmountVo vo = CommonConverter.map(newPo, StockAmountVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.GET })
    public JsonResult<StockVo> doQueryStockAmountDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待查询数量库存");
        StockUnion union = stockService.queryStockDetailWithAmount(id);
        StockVo vo = buildStockVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount", method = { RequestMethod.GET })
    public JsonResult<PagedList<StockVo>> doListPagedStockAmounts(StockQueryParams queryParams) {
        CheckHelper.checkPageParams(queryParams);
        StockQueryCondition condition = CommonConverter.map(queryParams, StockQueryCondition.class);
        PagedList<StockUnion> pagedUnions = stockService.listPagedStocksWithAmount(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<StockVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (StockUnion union : pagedUnions.getData()) {
            StockVo vo = buildStockVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    private StockVo buildStockVoByUnion(StockUnion union) {
        StockVo vo = CommonConverter.map(union.getStockPo(), StockVo.class);
        vo.setStockTypeName(union.getStockTypePo().getName());
        if (union.getStockAmountPo() != null) {
            vo.setAmount(union.getStockAmountPo().getAmount());
            vo.setStoreId(union.getStockAmountPo().getStoreId());
        }
        if (union.getStorePo() != null) {
            vo.setStoreName(union.getStorePo().getName());
        }
        return vo;
    }

}
