ALTER TABLE free_board_post
    ADD COLUMN deleted_at DATETIME(6) NULL AFTER updated_at;

-- 조회 성능을 위한 인덱스
CREATE INDEX idx_free_board_post_deleted_at
    ON free_board_post (deleted_at);