CREATE TABLE indexing_outbox (
                                 outbox_id       BIGINT NOT NULL AUTO_INCREMENT,

                                 aggregate_type  VARCHAR(50) NOT NULL,   -- FREE_BOARD_POST, QUESTION_POST
                                 aggregate_id    BIGINT NOT NULL,         -- post_id

                                 event_type      VARCHAR(30) NOT NULL,    -- UPSERT, DELETE
                                 status          VARCHAR(20) NOT NULL,    -- PENDING, PROCESSING, DONE, FAILED

                                 retry_count     INT NOT NULL DEFAULT 0,
                                 next_retry_at   DATETIME(6) NULL,

                                 created_at      DATETIME(6) NOT NULL,
                                 updated_at      DATETIME(6) NULL,

                                 PRIMARY KEY (outbox_id),
                                 KEY idx_outbox_status (status, next_retry_at),
                                 KEY idx_outbox_aggregate (aggregate_type, aggregate_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE free_board_ngram_posting (
                                          ngram   VARCHAR(10) NOT NULL,
                                          post_id BIGINT NOT NULL,
                                          PRIMARY KEY (ngram, post_id),
                                          KEY idx_free_board_ngram_post (post_id),
                                          CONSTRAINT fk_free_board_ngram_post
                                              FOREIGN KEY (post_id)
                                                  REFERENCES free_board_post (free_board_post_id)
                                                  ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;