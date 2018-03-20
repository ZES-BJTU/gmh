package com.zes.squad.gmh.service;

import java.util.List;

import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.entity.condition.StoreQueryCondition;
import com.zes.squad.gmh.entity.po.StorePo;
import com.zes.squad.gmh.entity.union.StoreUnion;

public interface StoreService {

    /**
     * 创建门店
     * 
     * @param po
     */
    StorePo createStore(StorePo po);

    /**
     * 删除单个门店
     * 
     * @param id
     */
    void removeStore(Long id);

    /**
     * 删除多个门店
     * 
     * @param ids
     */
    void removeStores(List<Long> ids);

    /**
     * 修改门店
     * 
     * @param po
     */
    StorePo modifyStore(StorePo po);

    /**
     * 查询门店详情
     * 
     * @param id
     * @return
     */
    StoreUnion queryStoreDetail(Long id);

    /**
     * 分页查询门店(支持模糊搜索)
     * 
     * @param condition
     * @return
     */
    PagedList<StoreUnion> listPagedStores(StoreQueryCondition condition);

    /**
     * 查询所有门店
     * 
     * @return
     */
    List<StorePo> listStores();

}
