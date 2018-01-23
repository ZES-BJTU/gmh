package com.zes.squad.gmh.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import static com.zes.squad.gmh.common.helper.LogicHelper.*;
import com.zes.squad.gmh.common.page.PagedLists;
import com.zes.squad.gmh.common.page.PagedLists.PagedList;
import com.zes.squad.gmh.context.ThreadContext;
import com.zes.squad.gmh.entity.condition.StockTypeQueryCondition;
import com.zes.squad.gmh.entity.po.StockTypePo;
import com.zes.squad.gmh.entity.po.UserPo;
import com.zes.squad.gmh.entity.union.StockTypeUnion;
import com.zes.squad.gmh.mapper.StockTypeMapper;
import com.zes.squad.gmh.mapper.StockTypeUnionMapper;
import com.zes.squad.gmh.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService {

    //    @Autowired
    //    private StockMapper          stockMapper;
    @Autowired
    private StockTypeMapper      stockTypeMapper;
    @Autowired
    private StockTypeUnionMapper stockTypeUnionMapper;

    @Override
    public void createStockType(StockTypePo po) {
        UserPo currentUserPo = ThreadContext.getUser();
        ensureEntityExist(currentUserPo, "获取当前用户失败");
        ensureParameterExist(currentUserPo.getStoreId(), "获取当前用户所属门店失败");
        po.setStoreId(currentUserPo.getStoreId());
        stockTypeMapper.insert(po);
    }

    @Override
    public void deleteStockType(Long id) {
        stockTypeMapper.deleteById(id);
    }

    @Override
    public void deleteStockTypes(List<Long> ids) {
        stockTypeMapper.batchDelete(ids);
    }

    @Override
    public void modifyStockType(StockTypePo po) {
        stockTypeMapper.updateSelective(po);
    }

    @Override
    public StockTypeUnion queryStockTypeDetail(Long id) {
        StockTypeUnion union = stockTypeUnionMapper.selectById(id);
        ensureEntityExist(union, "库存分类查询失败");
        return union;
    }

    @Override
    public PagedList<StockTypeUnion> queryStockTypesByCondition(StockTypeQueryCondition condition) {
        int pageNum = condition.getPageNum();
        int pageSize = condition.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<StockTypeUnion> unions = stockTypeUnionMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(unions)) {
            return PagedLists.newPagedList(pageNum, pageSize);
        }
        PageInfo<StockTypeUnion> pageInfo = new PageInfo<>(unions);
        return PagedLists.newPagedList(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), unions);
    }

}
