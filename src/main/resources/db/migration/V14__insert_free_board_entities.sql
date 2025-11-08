CREATE TABLE IF NOT EXISTS `free_board_post` (
                                                 `free_board_post_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                 `member_id`          BIGINT NOT NULL,
                                                 `title`              VARCHAR(255) NOT NULL,
                                                 `content`            LONGTEXT NOT NULL,
                                                 `views`              INT NOT NULL DEFAULT 0,
                                                 `number_of_recommendations` INT NOT NULL DEFAULT 0,
                                                 `number_of_comments` INT NOT NULL DEFAULT 0,
                                                 `created_at`         DATETIME(6) NOT NULL,
                                                 `updated_at`         DATETIME(6) NULL,
                                                 PRIMARY KEY (`free_board_post_id`),
                                                 KEY `fk_post_member` (`member_id`),
                                                 CONSTRAINT `fk_post_member`
                                                     FOREIGN KEY (`member_id`) REFERENCES `member_info` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 댓글 (자기참조 FK)
CREATE TABLE IF NOT EXISTS `free_board_post_reply` (
                                                       `free_board_post_reply_id` BIGINT NOT NULL AUTO_INCREMENT,
                                                       `member_id`                BIGINT NOT NULL,
                                                       `free_board_post_id`       BIGINT NOT NULL,
                                                       `parent_reply_id`          BIGINT NULL,
                                                       `content`                  LONGTEXT NOT NULL,
                                                       `created_at`               DATETIME(6) NOT NULL,
                                                       `updated_at`               DATETIME(6) NULL,
                                                       PRIMARY KEY (`free_board_post_reply_id`),

                                                       KEY `fk_reply_member` (`member_id`),
                                                       KEY `fk_reply_post` (`free_board_post_id`),
                                                       KEY `idx_reply_parent` (`parent_reply_id`),

                                                       CONSTRAINT `fk_reply_member`
                                                           FOREIGN KEY (`member_id`) REFERENCES `member_info` (`member_id`),

                                                       CONSTRAINT `fk_reply_post`
                                                           FOREIGN KEY (`free_board_post_id`) REFERENCES `free_board_post` (`free_board_post_id`),

    -- 자기참조 FK: 루트 댓글 삭제 안전하게 ON DELETE SET NULL 권장
                                                       CONSTRAINT `fk_reply_parent`
                                                           FOREIGN KEY (`parent_reply_id`)
                                                               REFERENCES `free_board_post_reply` (`free_board_post_reply_id`)
                                                               ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;