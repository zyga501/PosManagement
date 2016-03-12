package com.posmanagement.utils;

import com.opensymphony.xwork2.util.LocalizedTextUtil;

import java.util.Locale;

public class I18n {
    public static String GetText(String aTextName) {
        return LocalizedTextUtil.findDefaultText(aTextName, Locale.getDefault());
    }
}
