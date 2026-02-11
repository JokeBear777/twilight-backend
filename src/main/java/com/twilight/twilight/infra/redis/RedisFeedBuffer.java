package com.twilight.twilight.infra.redis;

import com.twilight.twilight.domain.bulletin.feed.FeedBuffer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisFeedBuffer implements FeedBuffer {

    private static final Duration TTL = Duration.ofMinutes(10);
    private static final int MAX_LEN = 500;

    private final RedisTemplate<String, String> redisTemplate;

    private String key(Long memberId) {
        return "feed:buffer:" + memberId;
    }



    @Override
    public List<Long> pop(Long memberId, int size) {
        if (memberId == null || size <= 0) return List.of();

        String k = key(memberId);

        List<String> popped = redisTemplate.opsForList().leftPop(k, size);
        if (popped == null || popped.isEmpty()) return List.of();

        List<Long> ids = new ArrayList<>(popped.size());
        for (String s : popped) {
            if (s == null) continue;
            try {
                ids.add(Long.parseLong(s));
            } catch (NumberFormatException ignore) {
                // 깨진 값은 스킵
            }
        }
        return ids;
    }

    @Override
    public void push(Long memberId, List<Long> eventIds) {
        if (memberId == null || eventIds == null || eventIds.isEmpty()) return;

        String k = key(memberId);
        List<String> values = eventIds.stream().map(String::valueOf).toList();

        redisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            byte[] kb = redisTemplate.getStringSerializer().serialize(k);

            // RPUSH
            for (String v : values) {
                byte[] vb = redisTemplate.getStringSerializer().serialize(v);
                conn.rPush(kb, vb);
            }

            // 최대 길이 제한 (최근 MAX_LEN개만 유지)
            conn.lTrim(kb, -MAX_LEN, -1);

            // TTL 갱신
            conn.pExpire(kb, TTL.toMillis());
            return null;
        });
    }

    @Override
    public boolean hasEnough(Long memberId, int size) {
        return false;
    }

    @Override
    public void clear(Long memberId) {

    }
}
