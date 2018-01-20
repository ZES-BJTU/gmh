package com.zes.squad.gmh.common.page;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分页操作帮助类(禁止直接使用PagedList类)
 * 
 * @author yuhuan.zhou 2018年1月13日 下午11:45:19
 */
public class PagedLists {

    private static final int DEFAULT_PAGE_NUM    = 1;
    private static final int DEFAULT_PAGE_SIZE   = 10;
    private static final int DEFAULT_TOTAL_COUNT = 0;

    public PagedLists() {
        throw new UnsupportedOperationException("PagedLists can not be instantiated!");
    }

    public static <T> PagedList<T> newPagedList() {
        return newPagedList(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);
    }

    public static <T> PagedList<T> newPagedList(int pageNum, int pageSize) {
        return newPagedList(pageNum, pageSize, DEFAULT_TOTAL_COUNT, null);
    }

    public static <T> PagedList<T> newPagedList(int pageNum, int pageSize, long totalCount, List<T> data) {
        PagedList<T> pagedList = new PagedList<>();
        pagedList.setPageNum(pageNum);
        pagedList.setPageSize(pageSize);
        pagedList.setTotalCount(totalCount);
        pagedList.setTotalPages((int) Math.ceil(((double) totalCount) / pageSize));
        pagedList.setData(data);
        return pagedList;
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PagedList<T> implements Serializable {

        private static final long serialVersionUID = 1L;
        private int               pageNum;
        private int               pageSize;
        private long              totalCount;
        private int               totalPages;
        private List<T>           data;

    }

}
