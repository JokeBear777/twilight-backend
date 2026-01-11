package com.twilight.twilight.global.policy;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectKeyGenerator {

    public String generateObject(Long userId, String filename) {
        return  "images/" + userId + "/" + UUID.randomUUID() + "_" + replaceFileName(filename);
    }

    private String replaceFileName(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "");
    }
}
