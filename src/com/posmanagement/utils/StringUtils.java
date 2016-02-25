package com.posmanagement.utils;

import java.text.DecimalFormat;

public class StringUtils {
    public static String convertNullableString(Object object) {
        if (object == null) {
            return "";
        }

        return object.toString();
    }

    public static String convertNullableString(Object object, String defaultValue) {
        if (object == null) {
            return defaultValue;
        }

        return object.toString();
    }

    public static boolean isEmpty(String string) {
        if (string == null || string.length() <= 0)
            return true;

        return false;
    }

    public static String formatCardNO(String originStr) {
        if (originStr.length() >= 8) {
            return originStr.substring(0, 4) + "****" + originStr.substring(originStr.length() - 4, originStr.length());
        }
        return originStr;
    }

    public static String formatMoney(String originStr) {
        try {
            return (new DecimalFormat("#0.00")).format(Float.parseFloat(originStr));
        }
        catch(Exception e) {
            return originStr;
        }
    }

}
