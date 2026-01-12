package com.twilight.twilight.domain.image.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UploadUrlRequestForm {
    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;     // image/png

    @Positive
    private long contentLength;
}
