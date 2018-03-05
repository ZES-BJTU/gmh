package com.zes.squad.gmh.web.helper;

import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationHelper {

    public static int toPageNum(int offset, int limit) {
        int pageNum = offset % 10;
        if (pageNum != 0) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETER,"分页页码错误");
        }
        return pageNum;
    }

}
