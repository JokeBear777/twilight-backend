package com.twilight.twilight.global.gateway.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRecommendationDlqPayload {
    private Long requestId;
    private Long memberId;
    private String reason;
    private int retryCount;
    private LocalDateTime failedAt;
    private AiRecommendationPayload payload;
}
