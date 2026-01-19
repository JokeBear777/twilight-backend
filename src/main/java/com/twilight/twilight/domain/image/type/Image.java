package com.twilight.twilight.domain.image.type;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "image")
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private String objectKey;

    @Enumerated(EnumType.STRING)
    private ImageStatus status;

    public void markUploaded() {
        if (status != ImageStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태에서만 업로드 완료 가능. 현재 상태: " + status);
        }
        status = ImageStatus.UPLOADED;
    }


    protected Image() {}

    public static Image createPending(
            Long ownerId,
            String objectKey
    ) {
        Image image = new Image();
        image.ownerId = ownerId;
        image.objectKey = objectKey;
        image.status = ImageStatus.PENDING;
        return image;
    }


    public void markDeleted() {
        if (this.status == ImageStatus.DELETED) return;
        this.status = ImageStatus.DELETED;
    }
}
