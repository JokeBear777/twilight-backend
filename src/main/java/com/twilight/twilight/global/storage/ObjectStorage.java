package com.twilight.twilight.global.storage;

public interface ObjectStorage {

    PresignedUploadUrl generatePresignedUploadUrl(String objectKey, Long contentLength,String contentType);
    void delete (String objectKey);

}
