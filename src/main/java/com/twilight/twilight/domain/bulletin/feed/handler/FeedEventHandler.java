package com.twilight.twilight.domain.bulletin.feed.handler;

import com.twilight.twilight.domain.bulletin.feed.dto.GetFeedPostListDto;
import com.twilight.twilight.domain.bulletin.feed.entity.FeedEvent;
import com.twilight.twilight.domain.bulletin.feed.type.FeedEventType;

import java.util.List;
import java.util.Map;

public interface FeedEventHandler {
    FeedEventType supports();

    Map<Long, GetFeedPostListDto> handle(List<FeedEvent> events);
}
