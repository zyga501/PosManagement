package com.posmanagement.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String generaterUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
