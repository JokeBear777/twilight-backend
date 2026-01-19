package com.twilight.twilight.domain.image.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UploadCompleteResponse {
    private final Long imageId;

    public static UploadCompleteResponse from(Long imageId) {
        return new UploadCompleteResponse(imageId);
    }
}