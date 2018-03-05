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
import com.zes.squad.gmh.entity.po.StockPo;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.union.StockUnion;
import com.zes.squad.gmh.service.StockService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.StockCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.StockQueryParams;
import com.zes.squad.gmh.web.entity.param.StockTypeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.StockTypeQueryParams;
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
    public JsonResult<StockTypeVo> doCreateStockType(@RequestBody StockTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "库存分类为空");
        ensureParameterExist(params.getName(), "库存分类名称为空");
        ensureParameterNotExist(params.getId(), "库存分类标识应为空");
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
    public JsonResult<StockTypeVo> doModifyStockType(@PathVariable("id") Long id,
                                                     @RequestBody StockTypeCreateOrModifyParams params) {
        ensureParameterExist(params, "库存分类为空");
        ensureParameterExist(id, "请选择待修改库存分类");
        ensureParameterValid(id.equals(params.getId()), "库存分类错误");
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
        StockTypeQueryCondition condition = CommonConverter.map(queryParams, StockTypeQueryCondition.class);
        PagedList<StockTypePo> pagedPos = stockService.listPagedStockTypes(condition);
        if (pagedPos == null || CollectionUtils.isEmpty(pagedPos.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedPos.getPageNum(), pagedPos.getPageSize()));
        }
        PagedList<StockTypeVo> pagedVos = CommonConverter.mapPagedList(pagedPos, StockTypeVo.class);
        return JsonResults.success(pagedVos);
    }

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<StockVo> doCreateStock(@RequestBody StockCreateOrModifyParams params) {
        ensureParameterExist(params, "库存信息为空");
        ensureParameterNotExist(params.getId(), "库存标识应为空");
        ensureParameterExist(params.getStockTypeId(), "库存分类为空");
        ensureParameterExist(params.getCode(), "库存代码为空");
        ensureParameterExist(params.getName(), "库存名称为空");
        ensureParameterExist(params.getTotalAmount(), "库存余量为空");
        ensureParameterValid(params.getTotalAmount().compareTo(BigDecimal.ZERO) == 1, "库存余量错误");
        StockPo po = CommonConverter.map(params, StockPo.class);
        stockService.createStock(po);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveStock(@PathVariable("id") Long id) {
        ensureParameterExist(id, "库存标识为空");
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
    public JsonResult<StockVo> doModifyStock(@PathVariable("id") Long id,
                                             @RequestBody StockCreateOrModifyParams params) {
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
        StockUnion union = stockService.queryStock(id);
        StockVo vo = buildStockVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<StockVo>> doListPagedStocks(StockQueryParams params) {
        CheckHelper.checkPageParams(params);
        StockQueryCondition condition = CommonConverter.map(params, StockQueryCondition.class);
        PagedList<StockUnion> pagedUnions = stockService.listPagedStocks(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success();
        }
        List<StockVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (StockUnion union : pagedUnions.getData()) {
            StockVo vo = buildStockVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/statistics", method = { RequestMethod.GET })
    public JsonResult<PagedList<StockVo>> doStatStocks() {
        return JsonResults.success();
    }

    @RequestMapping(path = "/consumption/records", method = { RequestMethod.PUT })
    public JsonResult<Void> doCreateStockConsumeRecord() {
        return JsonResults.success();
    }

    private StockVo buildStockVoByUnion(StockUnion union) {
        StockVo vo = CommonConverter.map(union.getStockPo(), StockVo.class);
        vo.setStockTypeName(union.getStockTypePo().getName());
        vo.setStoreName(union.getStoreName());
        return vo;
    }

}
