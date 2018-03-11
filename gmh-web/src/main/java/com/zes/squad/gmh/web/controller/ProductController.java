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

import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.ProductTypeQueryCondition;
import com.zes.squad.gmh.entity.po.ProductPo;
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.entity.union.ProductUnion;
import com.zes.squad.gmh.service.ProductService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ProductCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.ProductTypeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.StockTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.ProductTypeVo;
import com.zes.squad.gmh.web.entity.vo.ProductVo;
import com.zes.squad.gmh.web.helper.CheckHelper;
import com.zes.squad.gmh.web.helper.PaginationHelper;

@RequestMapping(path = "/products")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(path = "/types", method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<ProductTypeVo> doCreateProductType(@RequestBody ProductTypeCreateOrModifyParams params) {
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

    @RequestMapping(path = "/types", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProductTypes(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除产品分类");
        productService.removeProductTypes(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/types/{id}", method = { RequestMethod.PUT })
    public JsonResult<ProductTypeVo> doModifyProductType(@PathVariable("id") Long id,
                                                         @RequestBody ProductTypeCreateOrModifyParams params) {
        ensureParameterExist(id, "产品分类为空");
        ensureParameterExist(params, "产品分类为空");
        ensureParameterValid(id.equals(params.getId()), "产品分类错误");
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
        queryParams.setPageNum(PaginationHelper.toPageNum(queryParams.getOffset(), queryParams.getLimit()));
        queryParams.setPageSize(queryParams.getLimit());
        CheckHelper.checkPageParams(queryParams);
        ProductTypeQueryCondition condition = CommonConverter.map(queryParams, ProductTypeQueryCondition.class);
        PagedList<ProductTypePo> pagedPos = productService.listPagedProductTypes(condition);
        if (CollectionUtils.isEmpty(pagedPos.getData())) {
            return JsonResults.success(PagedLists.newPagedList(pagedPos.getPageNum(), pagedPos.getPageSize()));
        }
        PagedList<ProductTypeVo> pagedVos = CommonConverter.mapPagedList(pagedPos, ProductTypeVo.class);
        return JsonResults.success(pagedVos);
    }

    @RequestMapping(method = { RequestMethod.POST })
    public JsonResult<ProductVo> doCreateProduct(@RequestBody ProductCreateOrModifyParams params) {
        ensureParameterExist(params, "产品信息为空");
        ensureParameterNotExist(params.getId(), "产品已存在");
        ensureParameterExist(params.getProductTypeId(), "产品分类为空");
        ensureParameterExist(params.getName(), "产品名称为空");
        ensureParameterExist(params.getUnitName(), "产品计量单位为空");
        ensureParameterExist(params.getUnitPrice(), "产品单价为空");
        ensureParameterValid(params.getUnitPrice().compareTo(BigDecimal.ZERO) == 1, "产品单价必须大于0");
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

    @RequestMapping(method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveProducts(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除商品");
        productService.removeProducts(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<ProductVo> doModifyProducts(@PathVariable("id") Long id,
                                                  @RequestBody ProductCreateOrModifyParams params) {
        ensureParameterExist(id, "请选择待修改商品");
        ensureParameterExist(params, "请选择待修改商品");
        ensureParameterValid(id.equals(params.getId()), "待修改商品不存在");
        if (params.getUnitPrice() != null) {
            ensureParameterExist(params.getUnitPrice(), "产品单价为空");
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
    public JsonResult<Void> doListPagedProducts() {
        return JsonResults.success();
    }
    
    private ProductVo buildProductVoByUnion(ProductUnion union) {
        ProductVo vo = CommonConverter.map(union.getProductPo(), ProductVo.class);
        vo.setProductTypeName(union.getProductTypePo().getName());
        return vo;
    }

}
