package com.zes.squad.gmh.web.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
