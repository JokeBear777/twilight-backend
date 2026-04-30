package com.twilight.twilight.domain.book.entity.recommendation;

public enum RecommendationRequestStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    DLQ
}
