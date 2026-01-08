package com.twilight.twilight.global.storage;

import org.springframework.stereotype.Component;

import java.time.Duration;

public class PresignedUploadUrl {

    private final String uploadUrl;
    private final String objectKey;
    private final Duration expiresIn;

    public PresignedUploadUrl(String uploadUrl, String objectKey, Duration expiresIn) {
        this.uploadUrl = uploadUrl;
        this.objectKey = objectKey;
        this.expiresIn = expiresIn;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public Duration getExpiresIn() {
        return expiresIn;
    }

}
