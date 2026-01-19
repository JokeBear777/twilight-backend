package com.twilight.twilight.domain.image.dto;

import com.twilight.twilight.global.storage.PresignedUploadUrl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PresignedUploadUrlResponse {

    private final long imageId;
    private final String uploadUrl;
    private final String objectKey;
    private final long expiresInSeconds;

    public static PresignedUploadUrlResponse from(long imageId,PresignedUploadUrl url) {
        return new PresignedUploadUrlResponse(
                imageId,
                url.getUploadUrl(),
                url.getObjectKey(),
                url.getExpiresIn().getSeconds()
        );
    }
}
