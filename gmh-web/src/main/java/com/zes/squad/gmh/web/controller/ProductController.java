package com.zes.squad.gmh.web.controller;

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
import com.zes.squad.gmh.entity.condition.ProductQueryCondition;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductAmountPo;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.union.ProductUnion;
import com.zes.squad.gmh.service.ProductService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ProductAmountParams;
import com.zes.squad.gmh.web.entity.param.ProductConvertParams;
import com.zes.squad.gmh.web.entity.param.ProductParams;
import com.zes.squad.gmh.web.entity.param.ProductQueryParams;
import com.zes.squad.gmh.web.entity.param.ProductTypeParams;
import com.zes.squad.gmh.web.entity.param.StockTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.ProductAmountVo;
import com.zes.squad.gmh.web.entity.vo.ProductTypeVo;
import com.zes.squad.gmh.web.entity.vo.ProductVo;
import com.zes.squad.gmh.web.helper.CheckHelper;

@RequestMapping(path = "/products")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/types", method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ProductTypeVo> doCreateProductType(@RequestBody ProductTypeParams params) {
        ensureParameterExist(params, "产品分类为空");
        ensureParameterNotExist(params.getId(), "产品分类应为空");
        ensureParameterExist(params.getName(), "产品分类名称为空");
        ProductTypePo po = CommonConverter.map(params, ProductTypePo.class);
        ProductTypePo newTypePo = productService.createProductType(po);
        ProductTypeVo vo = CommonConverter.map(newTypePo, ProductTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProductType(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除产品分类");
        productService.removeProductType(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.PUT })
    public JsonResult<ProductTypeVo> doModifyProductType(@PathVariable("id") Long id,
                                                         @RequestBody ProductTypeParams params) {
        ensureParameterExist(id, "产品分类为空");
        ensureParameterExist(params, "产品分类为空");
        params.setId(id);
        ensureParameterExist(params.getName(), "产品分类名称为空");
        ProductTypePo po = CommonConverter.map(params, ProductTypePo.class);
        ProductTypePo newTypePo = productService.modifyProductType(po);
        ProductTypeVo vo = CommonConverter.map(newTypePo, ProductTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.GET })
    public JsonResult<ProductTypeVo> doQueryProductTypeDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除产品分类");
        ProductTypePo po = productService.queryProductTypeDetail(id);
        ProductTypeVo vo = CommonConverter.map(po, ProductTypeVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/types", method = { RequestMethod.GET })
    public JsonResult<PagedList<ProductTypeVo>> doListPagedProductTypes(StockTypeQueryParams queryParams) {
        ensureParameterExist(queryParams, "产品分类查询条件为空");
        CheckHelper.checkPageParams(queryParams);
        ProductTypeQueryCondition condition = CommonConverter.map(queryParams, ProductTypeQueryCondition.class);
        PagedList<ProductTypePo> pagedPos = productService.listPagedProductTypes(condition);
        if (CollectionUtils.isEmpty(pagedPos.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedPos.getPageNum(), pagedPos.getPageSize()));
        }
        PagedList<ProductTypeVo> pagedVos = CommonConverter.mapPagedList(pagedPos, ProductTypeVo.class);
        return JsonResults.success(pagedVos);
    }

    @RequestMapping(path = "/types/all", method = { RequestMethod.GET })
    public JsonResult<List<ProductTypeVo>> doListAllProductTypes() {
        List<ProductTypePo> pos = productService.listAllProductTypes();
        List<ProductTypeVo> vos = CommonConverter.mapList(pos, ProductTypeVo.class);
        return JsonResults.success(vos);
    }

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ProductVo> doCreateProduct(@RequestBody ProductParams params) {
        ensureParameterExist(params, "产品信息为空");
        ensureParameterNotExist(params.getId(), "产品已存在");
        ensureParameterExist(params.getProductTypeId(), "产品分类为空");
        ensureParameterExist(params.getCode(), "产品编码为空");
        ensureParameterExist(params.getName(), "产品名称为空");
        ensureParameterExist(params.getUnitName(), "产品计量单位为空");
        ensureParameterExist(params.getUnitPrice(), "产品单价为空");
        ensureParameterValid(params.getUnitPrice().compareTo(BigDecimal.ZERO) == 1, "产品单价应大于0");
        ProductPo po = CommonConverter.map(params, ProductPo.class);
        ProductPo newPo = productService.createProduct(po);
        ProductVo vo = CommonConverter.map(newPo, ProductVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProduct(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除商品");
        productService.removeProduct(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<ProductVo> doModifyProducts(@PathVariable("id") Long id, @RequestBody ProductParams params) {
        ensureParameterExist(id, "请选择待修改商品");
        ensureParameterExist(params, "请选择待修改商品");
        params.setId(id);
        if (params.getUnitPrice() != null) {
            ensureParameterValid(params.getUnitPrice().compareTo(BigDecimal.ZERO) == 1, "产品单价必须大于0");
        }
        ProductPo po = CommonConverter.map(params, ProductPo.class);
        ProductPo newPo = productService.modifyProduct(po);
        ProductVo vo = CommonConverter.map(newPo, ProductVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<ProductVo> doQueryProductDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择商品");
        ProductUnion union = productService.queryProductDetail(id);
        ProductVo vo = buildProductVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<ProductVo>> doListPagedProducts(ProductQueryParams params) {
        ensureParameterExist(params, "产品查询条件为空");
        CheckHelper.checkPageParams(params);
        ProductQueryCondition condition = CommonConverter.map(params, ProductQueryCondition.class);
        PagedList<ProductUnion> pagedUnions = productService.listPagedProducts(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ProductVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (ProductUnion union : pagedUnions.getData()) {
            ProductVo vo = buildProductVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/all", method = { RequestMethod.GET })
    public JsonResult<List<ProductVo>> doListAllProducts() {
        List<ProductPo> pos = productService.listAllProducts();
        List<ProductVo> vos = CommonConverter.mapList(pos, ProductVo.class);
        return JsonResults.success(vos);
    }

    @RequestMapping(path = "/store/all", method = { RequestMethod.GET })
    public JsonResult<List<ProductVo>> doListStoreAllProducts() {
        List<ProductPo> pos = productService.listStoreAllProducts();
        List<ProductVo> vos = CommonConverter.mapList(pos, ProductVo.class);
        return JsonResults.success(vos);
    }

    @RequestMapping(path = "/amount", method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ProductAmountVo> doCreateProductAmount(@RequestBody ProductAmountParams params) {
        ensureParameterExist(params, "产品数量为空");
        ensureParameterNotExist(params.getId(), "产品数量已存在");
        ensureParameterExist(params.getProductId(), "请选择产品");
        ensureParameterExist(params.getAmount(), "产品数量为空");
        ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "产品数量应大于0");
        ProductAmountPo po = CommonConverter.map(params, ProductAmountPo.class);
        ProductAmountPo newPo = productService.createProductAmount(po);
        ProductAmountVo vo = CommonConverter.map(newPo, ProductAmountVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProductAmount(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除数量产品");
        productService.removeProductAmount(id);
        return JsonResults.success();
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.PUT })
    public JsonResult<ProductAmountVo> doModifyProductAmount(@PathVariable("id") Long id,
                                                             @RequestBody ProductAmountParams params) {
        ensureParameterExist(id, "请选择待修改数量产品");
        ensureParameterExist(params, "请选择待修改数量产品");
        params.setId(id);
        if (params.getAmount() != null) {
            ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "待修改产品数量应大于0");
        }
        ProductAmountPo po = CommonConverter.map(params, ProductAmountPo.class);
        ProductAmountPo newPo = productService.modifyProductAmount(po);
        ProductAmountVo vo = CommonConverter.map(newPo, ProductAmountVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.PATCH })
    public JsonResult<ProductAmountVo> doAddProductAmount(@PathVariable("id") Long id,
                                                          @RequestBody ProductAmountParams params) {
        ensureParameterExist(id, "请选择待修改数量产品");
        ensureParameterExist(params, "请选择待修改数量产品");
        params.setId(id);
        if (params.getAmount() != null) {
            ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "产品数量应大于0");
        }
        ProductAmountPo po = CommonConverter.map(params, ProductAmountPo.class);
        ProductAmountPo newPo = productService.addProductAmount(po);
        ProductAmountVo vo = CommonConverter.map(newPo, ProductAmountVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount/{id}", method = { RequestMethod.GET })
    public JsonResult<ProductVo> doQueryProductDetailWithAmount(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待查询产品数量");
        ProductUnion union = productService.queryProductDetailWithAmount(id);
        ProductVo vo = buildProductVoByUnion(union);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/amount", method = { RequestMethod.GET })
    public JsonResult<PagedList<ProductVo>> doListPagedProductsWithAmount(ProductQueryParams queryParams) {
        ensureParameterExist(queryParams, "产品查询条件为空");
        CheckHelper.checkPageParams(queryParams);
        ProductQueryCondition condition = CommonConverter.map(queryParams, ProductQueryCondition.class);
        PagedList<ProductUnion> pagedUnions = productService.listPagedProductsWithAmount(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize()));
        }
        List<ProductVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (ProductUnion union : pagedUnions.getData()) {
            ProductVo vo = buildProductVoByUnion(union);
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    @RequestMapping(path = "/amount/convert/{productId}", method = { RequestMethod.PATCH })
    public JsonResult<Void> doConvertProductAmount(@PathVariable("productId") Long productId,
                                                   @RequestBody ProductConvertParams params) {
        ensureParameterExist(productId, "请选择产品");
        ensureParameterExist(params, "请选择产品");
        ensureParameterExist(params.getAmount(), "请输入产品转出数量");
        ensureParameterValid(params.getAmount().compareTo(BigDecimal.ZERO) == 1, "产品转出数量应大于0");
        ensureParameterExist(params.getToStoreId(), "请输入产品转入门店");
        params.setProductId(productId);
        ProductAmountPo po = CommonConverter.map(params, ProductAmountPo.class);
        po.setStoreId(params.getToStoreId());
        productService.convertProductAmount(po);
        return JsonResults.success();
    }

    private ProductVo buildProductVoByUnion(ProductUnion union) {
        ProductVo vo = CommonConverter.map(union.getProductPo(), ProductVo.class);
        vo.setProductTypeName(union.getProductTypePo().getName());
        if (union.getProductAmountPo() != null) {
            vo.setProductAmountId(union.getProductAmountPo().getId());
            vo.setAmount(union.getProductAmountPo().getAmount());
            vo.setStoreId(union.getProductAmountPo().getStoreId());
        }
        if (union.getStorePo() != null) {
            vo.setStoreName(union.getStorePo().getName());
        }
        return vo;
    }

}
