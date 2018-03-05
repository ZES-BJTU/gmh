package com.zes.squad.gmh.web.controller;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureCollectionNotEmpty;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
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

import com.google.common.collect.Lists;
import com.zes.squad.gmh.common.converter.CommonConverter;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StoreQueryCondition;
import com.zes.squad.gmh.entity.po.StorePo;
import com.zes.squad.gmh.entity.union.StoreUnion;
import com.zes.squad.gmh.service.StoreService;
import com.zes.squad.gmh.web.common.JsonResults;
import com.zes.squad.gmh.web.common.JsonResults.JsonResult;
import com.zes.squad.gmh.web.entity.param.StoreCreateOrModifyParams;
import com.zes.squad.gmh.web.entity.param.StoreQueryParams;
import com.zes.squad.gmh.web.entity.vo.StoreVo;
import com.zes.squad.gmh.web.helper.CheckHelper;
import com.zes.squad.gmh.web.helper.PaginationHelper;

@RequestMapping(path = "/stores/v1")
@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @RequestMapping(method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.CREATED)
    public JsonResult<StoreVo> doCreateStore(@RequestBody StoreCreateOrModifyParams params) {
        checkCreateStoreParams(params);
        StorePo po = CommonConverter.map(params, StorePo.class);
        StorePo newStorePo = storeService.createStore(po);
        StoreVo vo = CommonConverter.map(newStorePo, StoreVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveStore(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择待删除门店");
        storeService.removeStore(id);
        return JsonResults.success();
    }

    @RequestMapping(method = { RequestMethod.DELETE })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public JsonResult<Void> doRemoveStores(@RequestBody List<Long> ids) {
        ensureCollectionNotEmpty(ids, "请选择待删除门店");
        storeService.removeStores(ids);
        return JsonResults.success();
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.PUT })
    public JsonResult<StoreVo> doModifyStore(@PathVariable("id") Long id,
                                             @RequestBody StoreCreateOrModifyParams params) {
        checkModifyStoreParams(id, params);
        StorePo po = CommonConverter.map(params, StorePo.class);
        StorePo newStorePo = storeService.modifyStore(po);
        StoreVo vo = CommonConverter.map(newStorePo, StoreVo.class);
        return JsonResults.success(vo);
    }

    @RequestMapping(path = "/{id}", method = { RequestMethod.GET })
    public JsonResult<StoreVo> doQueryStoreDetail(@PathVariable("id") Long id) {
        ensureParameterExist(id, "请选择门店");
        StoreUnion union = storeService.queryStoreDetail(id);
        StoreVo vo = CommonConverter.map(union.getStorePo(), StoreVo.class);
        vo.setPrincipalName(union.getUserPo().getName());
        return JsonResults.success(vo);
    }

    @RequestMapping(method = { RequestMethod.GET })
    public JsonResult<PagedList<StoreVo>> doListStoresByPage(StoreQueryParams queryParams) {
        ensureParameterExist(queryParams, "门店查询条件为空");
        queryParams.setPageNum(PaginationHelper.toPageNum(queryParams.getOffset(), queryParams.getLimit()));
        queryParams.setPageSize(queryParams.getLimit());
        CheckHelper.checkPageParams(queryParams);
        StoreQueryCondition condition = CommonConverter.map(queryParams, StoreQueryCondition.class);
        PagedList<StoreUnion> pagedUnions = storeService.listStoresByPage(condition);
        if (CollectionUtils.isEmpty(pagedUnions.getData())) {
            return JsonResults.success();
        }
        List<StoreVo> vos = Lists.newArrayListWithCapacity(pagedUnions.getData().size());
        for (StoreUnion union : pagedUnions.getData()) {
            StoreVo vo = CommonConverter.map(union.getStorePo(), StoreVo.class);
            vo.setPrincipalName(union.getUserPo().getName());
            vos.add(vo);
        }
        return JsonResults.success(PagedLists.newPagedList(pagedUnions.getPageNum(), pagedUnions.getPageSize(),
                pagedUnions.getTotalCount(), vos));
    }

    private void checkCreateStoreParams(StoreCreateOrModifyParams params) {
        ensureParameterExist(params, "请选择待修改门店");
        ensureParameterExist(params.getName(), "门店名称为空");
        ensureParameterExist(params.getAddress(), "门店地址为空");
    }

    private void checkModifyStoreParams(Long id, StoreCreateOrModifyParams params) {
        ensureParameterExist(id, "请选择待修改门店");
        ensureParameterExist(params, "请选择待修改门店");
        ensureParameterValid(id.equals(params.getId()), "门店信息错误");
    }

}
