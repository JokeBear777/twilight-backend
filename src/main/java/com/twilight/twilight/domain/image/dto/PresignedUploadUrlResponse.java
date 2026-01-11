package com.twilight.twilight.domain.image.dto;

import com.twilight.twilight.global.storage.PresignedUploadUrl;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUploadUrlResponse {

    private String uploadUrl;
    private String objectKey;
    private long expiresInSeconds;

    public static PresignedUploadUrlResponse from(PresignedUploadUrl url) {
        return new PresignedUploadUrlResponse(
                url.getUploadUrl(),
                url.getObjectKey(),
                url.getExpiresIn().getSeconds()
        );
    }
}
