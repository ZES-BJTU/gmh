package com.zes.squad.gmh.web.helper;

import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationHelper {

    private static final int[] DEFAULT_LIMITS = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, };

    public static int toPageNum(int offset, int limit) {
        boolean isContained = false;
        for (int i = 0; i < DEFAULT_LIMITS.length;i++) {
            if (limit == DEFAULT_LIMITS[i]) {
                isContained = true;
                break;
            }
        }
        if (!isContained) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "分页大小错误");
        }
        int pageNum = offset % limit;
        if (pageNum != 0) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER, "分页页码错误");
        }
        return pageNum;
    }

}
