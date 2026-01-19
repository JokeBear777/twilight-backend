package com.twilight.twilight.global.util;

public class ContentNormalizer {


    public static String normalizeContent(String raw) {
        if (raw == null) return "";
        return raw.replace("\n", "<br>");
    }

}
