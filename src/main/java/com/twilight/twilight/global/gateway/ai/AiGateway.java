package com.twilight.twilight.global.gateway.ai;

import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationPayload;
import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationDlqPayload;

public interface AiGateway {

    void send(
        AiRecommendationPayload aiRecommendationPayload
    );

    void sendToDlq(
        AiRecommendationDlqPayload aiRecommendationDlqPayload
    );

}
