package com.twilight.twilight.domain.image.controller;

import com.twilight.twilight.domain.image.dto.PresignedUploadUrlResponse;
import com.twilight.twilight.domain.image.dto.UploadCompleteRequestForm;
import com.twilight.twilight.domain.image.dto.UploadUrlRequestForm;
import com.twilight.twilight.domain.image.service.ImageUploadService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageUploadService imageUploadService;

    //이미지 url 요청 http
    @PostMapping(
            value = "/url",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PresignedUploadUrlResponse> getUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("fileName") String fileName,
            @RequestParam("contentType") String contentType,
            @RequestParam("contentLength") long contentLength
    ) {
        UploadUrlRequestForm form = new UploadUrlRequestForm(fileName, contentType, contentLength);
        PresignedUploadUrlResponse res =
                imageUploadService.getUrl(form, userDetails.getMember().getMemberId());

        return ResponseEntity.ok(
                res
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

    @GetMapping("/{imageId}")
    public ResponseEntity<Resource> getImage(
            @PathVariable Long imageId
    ) {
        Resource resource = imageUploadService.getImage(imageId);

        return ResponseEntity.ok()
                //.contentType(MediaType.IMAGE_PNG) // or IMAGE_PNG
                .body(resource);
    }

}
