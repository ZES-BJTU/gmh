package com.zes.squad.gmh.service;

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
    void createStore(StorePo po);

    /**
     * 删除门店
     * 
     * @param id
     */
    void deleteStore(Long id);

    /**
     * 修改门店
     * 
     * @param po
     */
    void modifyStore(StorePo po);

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
    PagedList<StoreUnion> listStoresByPage(StoreQueryCondition condition);

}
