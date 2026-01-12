package com.twilight.twilight.domain.image.controller;

import com.twilight.twilight.domain.image.dto.PresignedUploadUrlResponse;
import com.twilight.twilight.domain.image.dto.UploadCompleteRequestForm;
import com.twilight.twilight.domain.image.dto.UploadUrlRequestForm;
import com.twilight.twilight.domain.image.service.ImageUploadService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import com.twilight.twilight.global.storage.PresignedUploadUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageUploadService imageUploadService;

    //이미지 url 요청 http
    @PostMapping("/url")
    public ResponseEntity<PresignedUploadUrlResponse> getUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute UploadUrlRequestForm form
    ) {
        PresignedUploadUrl presignedUrl =
                imageUploadService.getUrl(form, userDetails.getMember().getMemberId());

        return ResponseEntity.ok(
                PresignedUploadUrlResponse.from(presignedUrl)
        );
    }

    @PostMapping("/upload-complete")
    public ResponseEntity<Void> postUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UploadCompleteRequestForm form
    ) {
        imageUploadService.uploadComplete(
                form,
                userDetails.getMember().getMemberId()
        );

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{image-id}")
    public ResponseEntity<Void> deleteImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("image-id") Long imageId
    ) {
        imageUploadService.deleteImage(imageId, userDetails.getMember().getMemberId());
        return ResponseEntity.ok().build();
    }

}
