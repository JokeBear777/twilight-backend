-- feed_event: 시스템 이벤트 스트림 (피드 시간축)

CREATE TABLE feed_event (
                            feed_event_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            actor_id BIGINT NOT NULL,
                            event_type VARCHAR(50) NOT NULL,
                            target_id BIGINT NOT NULL,
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_feed_event_actor_created
    ON feed_event (actor_id, created_at DESC, feed_event_id DESC);