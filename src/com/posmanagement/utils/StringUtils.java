package com.posmanagement.utils;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static String maskCardno(String originStr) {
        return originStr.substring(0,4)+"****"+originStr.substring(originStr.length()-4,originStr.length());
    }


}
