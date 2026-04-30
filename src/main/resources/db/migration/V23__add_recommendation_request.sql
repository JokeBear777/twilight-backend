CREATE TABLE recommendation_request (
    id bigint NOT NULL AUTO_INCREMENT,
    member_id bigint NOT NULL,
    status varchar(30) NOT NULL,
    retry_count int NOT NULL DEFAULT 0,
    error_message longtext DEFAULT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    completed_at datetime(6) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_recommendation_request_member_created (member_id, created_at),
    KEY idx_recommendation_request_member_status_created (member_id, status, created_at)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
