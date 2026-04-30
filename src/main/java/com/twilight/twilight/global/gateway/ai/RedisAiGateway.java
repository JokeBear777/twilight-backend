package com.twilight.twilight.global.gateway.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationDlqPayload;
import com.twilight.twilight.global.gateway.ai.dto.AiRecommendationPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisAiGateway implements AiGateway {

    private static final String STREAM_KEY = "ai:recommend";
    private static final String DLQ_STREAM_KEY = "ai:recommend:dlq";
    private static final String GROUP_NAME = "ai-consumers";
    private static final String PRODUCER_ID = "spring-backend";

    private final RedisTemplate<String, Object> redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            ensureStreamAndGroup(STREAM_KEY, GROUP_NAME, true);
        } catch (Exception e) {
            log.error("Redis stream initialization failed", e);
        }
    }

    private void ensureStreamAndGroup(String key, String group, boolean latest) {
        try {
            var init = StreamRecords.mapBacked(Collections.singletonMap("_init", "1"))
                    .withStreamKey(key);
            redisTemplate.opsForStream().add(init);
        } catch (Exception e) {
            log.warn("Redis stream initial XADD failed. key={}, error={}", key, e.toString());
        }

        try {
            var groups = redisTemplate.opsForStream().groups(key);
            if (groups != null && groups.stream().anyMatch(g -> group.equals(g.groupName()))) {
                log.info("Group already exists. key={}, group={}", key, group);
                return;
            }
        } catch (Exception e) {
            log.debug("Redis stream group lookup failed. key={}, group={}, error={}", key, group, e.toString());
        }

        try {
            var offset = latest ? ReadOffset.latest() : ReadOffset.from("0-0");
            redisTemplate.opsForStream().createGroup(key, offset, group);
            log.info("Stream group created. key={}, group={}", key, group);
        } catch (Exception e) {
            if (isBusyGroup(e)) {
                log.info("Group already exists (race). key={}, group={}", key, group);
                return;
            }
            throw e;
        }
    }

    private boolean isBusyGroup(Throwable e) {
        while (e != null) {
            String msg = String.valueOf(e.getMessage());
            if (msg.contains("BUSYGROUP") || msg.contains("Consumer Group name already exists")) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

    @Override
    public void send(AiRecommendationPayload payload) {
        Long requestId = payload == null ? null : payload.getRequestId();
        try {
            String json = new ObjectMapper().writeValueAsString(payload);
            log.info("Payload JSON = {}", json);
            log.info("AI recommendation request payload requestId={}", requestId);
        } catch (JsonProcessingException e) {
            log.error("Payload serialization failed. requestId={}", requestId, e);
        }

        Map<String, Object> body = Map.of(
                "producer", PRODUCER_ID,
                "payload", payload
        );

        try {
            redisTemplate.opsForStream().add(STREAM_KEY, body);
        } catch (Exception e) {
            log.error("Redis Stream publish failed. streamKey={}, requestId={}", STREAM_KEY, requestId, e);
            throw e;
        }
    }

    @Override
    public void sendToDlq(AiRecommendationDlqPayload payload) {
        Long requestId = payload == null ? null : payload.getRequestId();
        try {
            String json = new ObjectMapper().writeValueAsString(payload);
            log.info("DLQ Payload JSON = {}", json);
        } catch (JsonProcessingException e) {
            log.error("DLQ payload serialization failed. requestId={}", requestId, e);
        }

        Map<String, Object> body = Map.of(
                "requestId", payload.getRequestId(),
                "memberId", payload.getMemberId(),
                "reason", payload.getReason(),
                "retryCount", payload.getRetryCount(),
                "failedAt", payload.getFailedAt().toString(),
                "payload", payload.getPayload()
        );

        try {
            redisTemplate.opsForStream().add(DLQ_STREAM_KEY, body);
        } catch (Exception e) {
            log.error("Redis DLQ Stream publish failed. streamKey={}, requestId={}", DLQ_STREAM_KEY, requestId, e);
            throw e;
        }
    }
}
