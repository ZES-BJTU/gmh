package com.zes.squad.gmh.web.helper;

import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterExist;
import static com.zes.squad.gmh.common.helper.LogicHelper.ensureParameterValid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zes.squad.gmh.web.entity.param.QueryParams;

public class CheckHelper {

    public static boolean isValidMobile(String mobile) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = pattern.matcher(mobile);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        if (email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }
        return true;
    }

    public static void checkPageParams(QueryParams params) {
        ensureParameterExist(params.getPageNum(), "分页页码为空");
        ensureParameterValid(params.getPageNum() > 0, "分页页码错误");
        ensureParameterExist(params.getPageSize(), "分页大小为空");
        ensureParameterValid(params.getPageSize() > 0, "分页大小错误");
    }

}
