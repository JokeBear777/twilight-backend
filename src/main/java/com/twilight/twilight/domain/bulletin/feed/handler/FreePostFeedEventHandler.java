package com.twilight.twilight.domain.bulletin.feed.handler;

import com.twilight.twilight.domain.bulletin.feed.dto.GetFeedPostListDto;
import com.twilight.twilight.domain.bulletin.feed.entity.FeedEvent;
import com.twilight.twilight.domain.bulletin.feed.type.FeedEventType;
import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;
import com.twilight.twilight.domain.bulletin.post.repository.FreeBoardPostQueryRepository;
import com.twilight.twilight.domain.bulletin.post.repository.FreeBoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FreePostFeedEventHandler implements FeedEventHandler {

    private final FreeBoardPostRepository freeBoardPostRepository;
    private final FreeBoardPostQueryRepository freeBoardPostQueryRepository;

    @Override
    public FeedEventType supports() {
        return FeedEventType.FREE_BOARD_POST_CREATED;
    }

    @Override
    public Map<Long, GetFeedPostListDto> handle(List<FeedEvent> events) {
        List<Long> postIds = events.stream()
                .map(FeedEvent::getTargetId)
                .toList();

        // 2. 벌크 조회 (IN 쿼리 1번)
        List<FreeBoardPost> posts =
                freeBoardPostQueryRepository.findAllByPostIds(postIds);

        // 3. Map 변환 (O(1) 접근용)
        Map<Long, FreeBoardPost> postMap =
                posts.stream()
                        .collect(Collectors.toMap(
                                FreeBoardPost::getFreeBoardPostId,
                                Function.identity()
                        ));

        return events.stream()
                .collect(Collectors.toMap(
                        FeedEvent::getId,
                        e -> GetFeedPostListDto.fromFreeBoardPost(
                                postMap.get(e.getTargetId())
                        )
                ));
    }
}
