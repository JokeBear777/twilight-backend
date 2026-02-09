package com.twilight.twilight.domain.bulletin.feed;

import java.util.List;

public interface FeedBuffer {
    List<Long> pop(Long memberId, int size);

    void push(Long memberId, List<Long> feedEventIds);

    boolean hasEnough(Long memberId, int size);

    void clear(Long memberId);
}
