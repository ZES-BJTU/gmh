package com.zes.squad.gmh.helper;

public class SMSHelper {

    public static String generateAuthCode(int length) {
        String digits = "0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * (digits.length()));
            builder.append(digits.substring(index, index + 1));
        }
        return builder.toString();
    }

    public static void sendMessage(String mobile, String content) {

    }

}
