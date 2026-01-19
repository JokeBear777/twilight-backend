package com.twilight.twilight.domain.image.service;

import com.twilight.twilight.domain.image.dto.PresignedUploadUrlResponse;
import com.twilight.twilight.domain.image.dto.UploadCompleteRequestForm;
import com.twilight.twilight.domain.image.dto.UploadUrlRequestForm;
import com.twilight.twilight.domain.image.repository.ImageRepository;
import com.twilight.twilight.domain.image.type.Image;
import com.twilight.twilight.domain.image.type.ImageStatus;
import com.twilight.twilight.global.policy.ObjectKeyGenerator;
import com.twilight.twilight.global.storage.ObjectStorage;
import com.twilight.twilight.global.storage.PresignedUploadUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageUploadService {

    private final ObjectKeyGenerator objectKeyGenerator;
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB, 환경변수로 수정필요
    private final ObjectStorage objectStorage;
    private final ImageRepository imageRepository;

    @Transactional
    public PresignedUploadUrlResponse getUrl(UploadUrlRequestForm form, Long userId) {
        validateRequestUploadUrlForm(form);
        String objectKey = objectKeyGenerator.generateObject(userId, form.getFileName());
        Image image = imageRepository.save(
                Image.createPending(userId, objectKey)
        );

        return PresignedUploadUrlResponse.from(
                image.getId(),
                objectStorage.generatePresignedUploadUrl(
                        objectKey,
                        form.getContentLength(),
                        form.getContentType()
                )
        );
    }

    private void validateRequestUploadUrlForm(UploadUrlRequestForm form) {
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

    @Transactional
    public void uploadComplete(UploadCompleteRequestForm form, Long userId) {
        if (form.getObjectKey() == null || form.getObjectKey().isBlank()) {
            throw new IllegalArgumentException("ObjectKey 필요");
        }

        Image image = imageRepository.findByObjectKey(form.getObjectKey())
                .orElseThrow(() -> new IllegalArgumentException("이미지 없음, Object Key: " + form.getObjectKey()));

        if (!image.getOwnerId().equals(userId)) {
            throw new SecurityException("권한 없음");
        }

        image.markUploaded();
    }

    @Transactional
    public void deleteImage(Long imageId, Long userId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지 없음, Image Id: " + imageId));
        if (image.getOwnerId().equals(userId)) {
            throw new SecurityException("권한 없음");
        }

        image.markDeleted();
    }

    public Resource getImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지 없음, Image Id: " + imageId));
        if (image.getStatus() != ImageStatus.UPLOADED) {
            throw new IllegalStateException("이미지 업로드 미완료");
        }

        return objectStorage.load(image.getObjectKey());
    }



}
