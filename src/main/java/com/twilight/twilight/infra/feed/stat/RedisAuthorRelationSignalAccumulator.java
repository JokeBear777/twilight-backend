package com.twilight.twilight.infra.feed.stat;

import com.twilight.twilight.domain.bulletin.feed.stat.AuthorRelationSignalAccumulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisAuthorRelationSignalAccumulator implements AuthorRelationSignalAccumulator {

    private static final int PROMOTION_THRESHOLD = 3;

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public boolean accumulateViewSignal(Long viewerId, Long authorId) {
        if (viewerId == null || authorId == null) {
            return false;
        }

        String key = "view:" + viewerId + ":" + authorId;

        Integer count = redisTemplate.opsForValue().increment(key).intValue();
        redisTemplate.expire(key, Duration.ofDays(7));

        //log.info("[Test] : count = {}", count);
        return count >= PROMOTION_THRESHOLD;
    }
}
