package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterNotExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

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
import com.zes.squad.gmh.entity.po.ProductTypePo;
import com.zes.squad.gmh.service.ProductService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.ProductTypeCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.StockTypeQueryParams;
import com.zes.squad.gmh.web.entity.vo.ProductTypeVo;
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
        ensureParameterExist(ids, "请选择待删除产品分类");
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

}
