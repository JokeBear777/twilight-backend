package com.twilight.twilight.global.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Profile("local")
public class MockObjectStorage implements ObjectStorage {
    @Override
    public void delete(String objectKey) {
        //mock 이므로 미구현
    }

    @Override
    public PresignedUploadUrl generatePresignedUploadUrl(
            String objectKey,
            Long contentLength,
            String contentType) {
        //String mockUrl = "https://mock-storage/upload/" + objectKey;
        String mockUrl = "https://mond-al.github.io/assets/images/forTest/ratio/all_ratio/image_3_320x240.png";


        return new PresignedUploadUrl(
                mockUrl,
                objectKey,
                Duration.ofMinutes(5)
        );
    }
}
