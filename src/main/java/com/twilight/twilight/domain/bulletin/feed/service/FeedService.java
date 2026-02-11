package com.twilight.twilight.domain.bulletin.feed.service;

import com.twilight.twilight.domain.bulletin.feed.FeedBuffer;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursor;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursorRequest;
import com.twilight.twilight.domain.bulletin.feed.dto.GetFeedPostListDto;
import com.twilight.twilight.domain.bulletin.feed.entity.FeedEvent;
import com.twilight.twilight.domain.bulletin.feed.handler.FeedEventHandler;
import com.twilight.twilight.domain.bulletin.feed.heuristic.FeedHeuristicCandidate;
import com.twilight.twilight.domain.bulletin.feed.heuristic.RelationScoreCalculator;
import com.twilight.twilight.domain.bulletin.feed.repository.FeedQueryRepository;
import com.twilight.twilight.domain.bulletin.feed.repository.FeedEventRepository;
import com.twilight.twilight.domain.bulletin.feed.stat.AuthorViewStatService;
import com.twilight.twilight.domain.bulletin.feed.type.FeedEventType;
import com.twilight.twilight.global.cursor.CursorResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class FeedService {

    private final FeedEventRepository feedEventRepository;
    private final FeedQueryRepository feedQueryRepository;
    private final FeedBuffer feedBuffer;
    private final AuthorViewStatService authorViewStatService;
    private final RelationScoreCalculator relationScoreCalculator;
    private final Map<FeedEventType, FeedEventHandler> handlerMap;

    public FeedService(
            FeedEventRepository feedEventRepository,
            FeedQueryRepository feedQueryRepository,
            FeedBuffer feedBuffer,
            AuthorViewStatService authorViewStatService,
            RelationScoreCalculator relationScoreCalculator,
            List<FeedEventHandler> handlers
    ) {
        this.feedEventRepository = feedEventRepository;
        this.feedQueryRepository = feedQueryRepository;
        this.feedBuffer = feedBuffer;
        this.authorViewStatService = authorViewStatService;
        this.relationScoreCalculator = relationScoreCalculator;
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        FeedEventHandler::supports,
                        Function.identity()
                ));
    }

    public void createFeedEvent(
            Long actorId,
            FeedEventType eventType,
            Long targetId
    ) {
        feedEventRepository.save(new FeedEvent(actorId, eventType, targetId));
    }

    public void deleteFeedEvent(Long targetId) {
        feedEventRepository.deleteById(targetId);
    }


    //여기서 버퍼있는지 확인하고 없으면 db에서 조회
    public List<GetFeedPostListDto> getFeedByCursor(
            FeedCursorRequest cursorRequest,
            Long memberId
    ) {
        FeedCursor cursor = cursorRequest.toCursor();
        int pageSize = cursorRequest.pageSizeOrDefault();

        List<Long> eventIds = feedBuffer.pop(memberId, pageSize);

        //log.info("[Test] eventIds = {}", eventIds);

        if (eventIds.size() < pageSize) {
            List<FeedHeuristicCandidate> candidates = feedQueryRepository.findHeuristicCandidatesByCursor(
                    cursor,
                    memberId,
                    pageSize * 10
            );

            //log.info("[Test] candidates = {}", candidates);

            List<Long> getBestEvents = selectBestEventByHeuristic(candidates, memberId);

            //log.info("[Test] bestEvents = {}", getBestEvents);
            feedBuffer.push(memberId, getBestEvents);
        }

        return assemble(eventIds);
    }

    public CursorResponse<GetFeedPostListDto> getCursorResponse(
            List<GetFeedPostListDto> feedPostListDto,
            int pageSize
            ) {
        boolean hasNext = feedPostListDto.size() > pageSize;
        FeedCursor nextFeedCursor = null;

        if (hasNext) {
            GetFeedPostListDto last = feedPostListDto.get(feedPostListDto.size() - 1);
            nextFeedCursor = new FeedCursor(last.postId(), last.createdAt());
            feedPostListDto.remove(feedPostListDto.size() - 1);
        }

        return new CursorResponse<>(feedPostListDto, nextFeedCursor, hasNext);
    }

    private List<Long> selectBestEventByHeuristic(
            List<FeedHeuristicCandidate> candidates,
            Long viewerId
    )  {
        if (candidates.isEmpty()) {
            return List.of();
        }

        List<Long> authorIds = candidates.stream()
                .map(FeedHeuristicCandidate::targetId)
                .distinct()
                .toList();

        Map<Long, Integer> relationScoreMap =
                authorViewStatService.getRelationScore(viewerId, authorIds);

        return relationScoreCalculator.relationScoreCalculate(candidates, relationScoreMap);
    }

    private List<GetFeedPostListDto> assemble(List<Long> eventIds) {
        if (eventIds.isEmpty()) {
            return List.of();
        }

        List<FeedEvent> events =
                feedEventRepository.findAllById(eventIds);


        Map<FeedEventType, List<FeedEvent>> grouped =
                events.stream()
                        .collect(Collectors.groupingBy(FeedEvent::getEventType));

        Map<Long, GetFeedPostListDto> dtoByEventId = new HashMap<>();

        for (var entry : grouped.entrySet()) {
            FeedEventHandler handler = handlerMap.get(entry.getKey());
            if (handler != null) {
                dtoByEventId.putAll(handler.handle(entry.getValue()));
            }
        }

        return eventIds.stream()
                .map(dtoByEventId::get)
                .filter(Objects::nonNull)
                .toList();
    }
}
