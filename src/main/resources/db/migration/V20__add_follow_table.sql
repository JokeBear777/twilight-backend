CREATE TABLE follow (
                        follow_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        follower_id BIGINT NOT NULL,
                        following_id BIGINT NOT NULL,
                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        UNIQUE KEY uk_follow (follower_id, following_id),
                        KEY idx_following (following_id),

                        CONSTRAINT fk_follow_follower
                            FOREIGN KEY (follower_id) REFERENCES member_info(member_id),
                        CONSTRAINT fk_follow_following
                            FOREIGN KEY (following_id) REFERENCES member_info(member_id)
) ENGINE=InnoDB;

CREATE INDEX idx_post_member_created
    ON free_board_post (member_id, deleted_at ,created_at DESC, free_board_post_id DESC);


