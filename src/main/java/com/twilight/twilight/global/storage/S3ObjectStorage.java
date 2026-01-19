package com.twilight.twilight.global.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class S3ObjectStorage implements ObjectStorage {
    @Override
    public PresignedUploadUrl generatePresignedUploadUrl(String objectKey, Long contentLength, String contentType) {
        return null;
    }

    @Override
    public void delete(String objectKey) {

    }

    @Override
    public Resource load(String objectKey) {

        return null;
    }
}
