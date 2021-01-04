package com.maestro.app.utils;

import java.util.UUID;

public class CommonUtils {
    public static String generateGuid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
