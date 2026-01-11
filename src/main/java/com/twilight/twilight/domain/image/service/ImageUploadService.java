package com.twilight.twilight.domain.image.service;

import com.twilight.twilight.domain.image.dto.RequestUploadUrlForm;
import com.twilight.twilight.global.policy.ObjectKeyGenerator;
import com.twilight.twilight.global.storage.ObjectStorage;
import com.twilight.twilight.global.storage.PresignedUploadUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final ObjectKeyGenerator objectKeyGenerator;
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB, 환경변수로 수정필요
    private final ObjectStorage objectStorage;

    public PresignedUploadUrl getUrl(RequestUploadUrlForm form, Long userId) {
        validateRequestUploadUrlForm(form, userId);


        return objectStorage.generatePresignedUploadUrl(
                objectKeyGenerator.generateObject(userId, form.getFileName()),
                form.getContentLength(),
                form.getContentType()
                );
    }

    private void validateRequestUploadUrlForm(RequestUploadUrlForm form, Long userId) {
        if (form.getFileName() == null || form.getFileName().isBlank()) {
            throw new IllegalArgumentException("파일 이름 필요");
        }

        if (form.getContentType() == null ||
                !form.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능");
        }

        if (form.getContentLength() <= 0 ||
                form.getContentLength() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("파일 크기 초과");
        }
    }

}
