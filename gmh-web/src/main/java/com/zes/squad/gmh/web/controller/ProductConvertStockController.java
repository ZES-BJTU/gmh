package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
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
import com.zes.squad.gmh.entity.condition.ProductConvertStockQueryCondition;
import com.zes.squad.gmh.entity.po.ProductConvertStockPo;
import com.zes.squad.gmh.entity.union.ProductConvertStockUnion;
import com.zes.squad.gmh.service.ProductConvertStockService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ProductConvertStockParams;
import com.zes.squad.gmh.web.entity.param.ProductConvertStockQueryParams;
import com.zes.squad.gmh.web.entity.vo.ProductConvertStockVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/products/stocks")
@RestController
public class ProductConvertStockController {

    @Autowired
    private ProductConvertStockService productConvertStockService;

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ProductConvertStockVo> doCreateProductConvertStock(@RequestBody ProductConvertStockParams params) {
        ensureParameterExist(params, "请输入产品库存转换关系");
        ensureParameterExist(params.getProductId(), "请选择产品");
        ensureParameterExist(params.getProductAmount(), "请输入产品库存转换关系产品最小数量");
        ensureParameterValid(params.getProductAmount().compareTo(BigDecimal.ZERO) == 1, "产品数量错误");
        ensureParameterExist(params.getStockId(), "请选择库存");
        ensureParameterExist(params.getStockAmount(), "请输入产品库存转换关系库存最小数量");
        ensureParameterValid(params.getStockAmount().compareTo(BigDecimal.ZERO) == 1, "库存数量错误");
        ProductConvertStockPo po = CommonConverter.map(params, ProductConvertStockPo.class);
        ProductConvertStockPo newPo = productConvertStockService.createProductConvertStock(po);
        ProductConvertStockVo vo = CommonConverter.map(newPo, ProductConvertStockVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProductConvertStock(@PathVariable("id") Long id) {
        productConvertStockService.removeProductConvertStock(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "{id}", method = { RequestMethod.PUT })
    public JsonResult<ProductConvertStockVo> doModifyProductConvertStock(@PathVariable("id") Long id,
                                                                         @RequestBody ProductConvertStockParams params) {
        ensureParameterExist(id, "请选择产品库存转换关系");
        ensureParameterExist(params, "请输入产品库存转换关系");
        ensureParameterExist(params.getProductId(), "请选择产品");
        if (params.getProductAmount() != null) {
            ensureParameterValid(params.getProductAmount().compareTo(BigDecimal.ZERO) == 1, "产品数量错误");
        }
        ensureParameterExist(params.getStockId(), "请选择库存");
        if (params.getStockAmount() != null) {
            ensureParameterValid(params.getStockAmount().compareTo(BigDecimal.ZERO) == 1, "库存数量错误");

        }
        ProductConvertStockPo po = CommonConverter.map(params, ProductConvertStockPo.class);
        ProductConvertStockPo newPo = productConvertStockService.modifyProductConvertStock(po);
        ProductConvertStockVo vo = CommonConverter.map(newPo, ProductConvertStockVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<ProductConvertStockVo>> doListPagedProductConvertStocks(ProductConvertStockQueryParams queryParams) {
        CheckHelper.checkPageParams(queryParams);
        ProductConvertStockQueryCondition condition = CommonConverter.map(queryParams,
                ProductConvertStockQueryCondition.class);
        PagedList<ProductConvertStockUnion> pagedUnions = productConvertStockService
                .listPagedProductConvertStocks(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ProductConvertStockVo> vos = buildProductConvertStockVosByUnions(pagedUnions.getData());
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/all", method = { RequestMethod.GET })
    public JsonResult<List<ProductConvertStockVo>> doCreateProductConvertStock() {
        List<ProductConvertStockUnion> unions = productConvertStockService.listAllProductConvertStocks();
        if (CollectionUtils.isEmpty(unions)) {
            return JsonResults.success();
        }
        List<ProductConvertStockVo> vos = buildProductConvertStockVosByUnions(unions);
        return JsonResults.success(vos);
    }

    private List<ProductConvertStockVo> buildProductConvertStockVosByUnions(List<ProductConvertStockUnion> unions) {
        List<ProductConvertStockVo> vos = Lists.newArrayListWithCapacity(unions.size());
        for (ProductConvertStockUnion union : unions) {
            ProductConvertStockVo vo = CommonConverter.map(union.getProductConvertStockPo(),
                    ProductConvertStockVo.class);
            vo.setProductTypeId(union.getProductTypePo().getId());
            vo.setProductTypeName(union.getProductTypePo().getName());
            vo.setProductCode(union.getProductPo().getCode());
            vo.setProductName(union.getProductPo().getName());
            vo.setProductUnitName(union.getProductPo().getUnitName());
            vo.setProductUnitPrice(union.getProductPo().getUnitPrice());
            vo.setStockTypeId(union.getStockTypePo().getId());
            vo.setStockTypeName(union.getStockTypePo().getName());
            vo.setStockCode(union.getStockPo().getCode());
            vo.setStockName(union.getStockPo().getName());
            vo.setStockUnitName(union.getStockPo().getUnitName());
            vos.add(vo);
        }
        return vos;
    }

}
