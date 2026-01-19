CREATE TABLE image (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       owner_id BIGINT NOT NULL,
                       object_key VARCHAR(255) NOT NULL,
                       status VARCHAR(20) NOT NULL,

                       PRIMARY KEY (id),
                       KEY idx_image_owner_id (owner_id),
                       KEY idx_image_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;