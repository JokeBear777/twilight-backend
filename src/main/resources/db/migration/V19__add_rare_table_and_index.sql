CREATE TABLE tag_stats (
                           tag_id BIGINT PRIMARY KEY,
                           usage_count BIGINT NOT NULL,
                           updated_at DATETIME NOT NULL
);

##복합 인덱스 추가
ALTER TABLE book_tags
    ADD CONSTRAINT uk_tag_book UNIQUE (tag_id, book_id);