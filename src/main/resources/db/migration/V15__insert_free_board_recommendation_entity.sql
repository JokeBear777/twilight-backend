CREATE TABLE IF NOT EXISTS free_board_post_recommendation (
                                                              id BIGINT NOT NULL AUTO_INCREMENT,
                                                              free_board_post_id BIGINT NOT NULL,
                                                              member_id BIGINT NOT NULL,
                                                              created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

                                                              CONSTRAINT pk_free_board_post_recommendation PRIMARY KEY (id),
                                                              CONSTRAINT uk_post_member UNIQUE (free_board_post_id, member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) 조회 인덱스
CREATE INDEX idx_fbpr_post   ON free_board_post_recommendation(free_board_post_id);
CREATE INDEX idx_fbpr_member ON free_board_post_recommendation(member_id);

ALTER TABLE free_board_post_recommendation
    ADD CONSTRAINT fk_fbpr_post
        FOREIGN KEY (free_board_post_id)
            REFERENCES free_board_post (free_board_post_id)
            ON DELETE CASCADE;

ALTER TABLE free_board_post_recommendation
    ADD CONSTRAINT fk_fbpr_member
        FOREIGN KEY (member_id)
            REFERENCES member_info (member_id)
            ON DELETE CASCADE;