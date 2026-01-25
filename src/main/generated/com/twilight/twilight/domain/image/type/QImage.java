package com.twilight.twilight.domain.image.type;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QImage is a Querydsl query type for Image
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImage extends EntityPathBase<Image> {

    private static final long serialVersionUID = 337519095L;

    public static final QImage image = new QImage("image");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath objectKey = createString("objectKey");

    public final NumberPath<Long> ownerId = createNumber("ownerId", Long.class);

    public final EnumPath<ImageStatus> status = createEnum("status", ImageStatus.class);

    public QImage(String variable) {
        super(Image.class, forVariable(variable));
    }

    public QImage(Path<? extends Image> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImage(PathMetadata metadata) {
        super(Image.class, metadata);
    }

}

