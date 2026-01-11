package com.twilight.twilight.domain.image.type;

import jakarta.persistence.*;

@Entity(name = "image")
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private Long ownerId;

    private String objectKey;

    @Enumerated(EnumType.STRING)
    private ImageStatus status;

    public void markUploaded() {
        if (status != ImageStatus.PENDING) {
            throw new IllegalStateException();
        }
        status = ImageStatus.UPLOADED;
    }


    protected Image() {}

    public static Image createPending(
            Long ownerId,
            String objectKey,
            String contentType,
            long contentLength
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
