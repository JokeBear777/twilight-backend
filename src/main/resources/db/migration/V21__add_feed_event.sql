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

CREATE TABLE author_view_stat (
                                  viewer_id BIGINT NOT NULL,
                                  author_id BIGINT NOT NULL,
                                  view_count INT NOT NULL DEFAULT 1,
                                  last_viewed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  PRIMARY KEY (viewer_id, author_id)
);

CREATE INDEX idx_author_view_stat_viewer
    ON author_view_stat (viewer_id);

CREATE INDEX idx_author_view_stat_last_viewed
    ON author_view_stat (last_viewed_at);