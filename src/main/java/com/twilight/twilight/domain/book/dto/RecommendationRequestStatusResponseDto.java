package com.twilight.twilight.domain.book.dto;

import com.twilight.twilight.domain.book.entity.recommendation.RecommendationRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequestStatusResponseDto {
    private Long requestId;
    private RecommendationRequestStatus status;
    private boolean completed;
    private int retryCount;
    private String message;
}
