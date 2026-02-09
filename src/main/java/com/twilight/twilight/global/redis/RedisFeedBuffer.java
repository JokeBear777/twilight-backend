package com.twilight.twilight.global.redis;

import com.twilight.twilight.domain.bulletin.feed.FeedBuffer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisFeedBuffer implements FeedBuffer {
    @Override
    public List<Long> pop(Long memberId, int size) {
        return List.of();
    }

    @Override
    public void push(Long memberId, List<Long> feedEventIds) {

    }

    @Override
    public boolean hasEnough(Long memberId, int size) {
        return false;
    }

    @Override
    public void clear(Long memberId) {

    }
}
