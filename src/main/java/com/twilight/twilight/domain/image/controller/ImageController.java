package com.twilight.twilight.domain.image.controller;

import com.twilight.twilight.domain.bulletin.post.dto.FreeBoardPostEditForm;
import com.twilight.twilight.domain.image.dto.PresignedUploadUrlResponse;
import com.twilight.twilight.domain.image.dto.RequestUploadUrlForm;
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
    @PostMapping("upload-url")
    public ResponseEntity<PresignedUploadUrlResponse> getUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute RequestUploadUrlForm form
    ) {
        PresignedUploadUrl presignedUrl =
                imageUploadService.getUrl(form, userDetails.getMember().getMemberId());


        return ResponseEntity.ok(
                PresignedUploadUrlResponse.from(presignedUrl)
        );
    }


    //이미지 url 업로드 완료 http
    @PostMapping("/url/")
    public ResponseEntity<?> postUrl() {
        return null;
    }


    //이미지 url 삭제 http
    @DeleteMapping("/url")
    public ResponseEntity<?> deleteUrl() {
        return null;
    }

}
