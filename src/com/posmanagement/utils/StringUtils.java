package com.posmanagement.utils;

public class StringUtils {
    public static String convertNullableString(Object object) {
        if (object == null) {
            return "";
        }

        return object.toString();
    }

    public static boolean isEmpty(String string) {
        if (string == null || string.length() <= 0)
            return true;

        return false;
    }
}
