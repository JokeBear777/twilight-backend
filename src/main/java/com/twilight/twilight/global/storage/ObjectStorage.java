package com.twilight.twilight.global.storage;

import org.springframework.core.io.Resource;

public interface ObjectStorage {

    PresignedUploadUrl generatePresignedUploadUrl(String objectKey, Long contentLength,String contentType);
    void delete (String objectKey);
    Resource load (String objectKey);

}
