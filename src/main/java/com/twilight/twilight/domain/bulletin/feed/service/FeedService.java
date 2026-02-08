package com.twilight.twilight.domain.bulletin.feed.service;

import com.twilight.twilight.domain.bulletin.feed.FeedBuffer;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursor;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursorRequest;
import com.twilight.twilight.domain.bulletin.feed.dto.GetFeedPostListDto;
import com.twilight.twilight.domain.bulletin.feed.heuristic.FeedHeuristicCandidate;
import com.twilight.twilight.domain.bulletin.feed.heuristic.RelationScoreCalculator;
import com.twilight.twilight.domain.bulletin.feed.repository.FeedQueryRepository;
import com.twilight.twilight.domain.bulletin.feed.repository.FeedRepository;
import com.twilight.twilight.domain.bulletin.feed.stat.AuthorViewStatService;
import com.twilight.twilight.global.cursor.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedQueryRepository feedQueryRepository;
    private final FeedBuffer feedBuffer;
    private final AuthorViewStatService authorViewStatService;
    private final RelationScoreCalculator relationScoreCalculator;


    //여기서 버퍼있는지 확인하고 없으면 db에서 조회
    public List<GetFeedPostListDto> getFeedByCursor(
            FeedCursorRequest cursorRequest,
            Long memberId
    ) {
        FeedCursor cursor = cursorRequest.toCursor();
        int pageSize = cursorRequest.pageSizeOrDefault();

        List<Long> eventIds = feedBuffer.pop(memberId, pageSize);

        if (eventIds.size() < pageSize) {
            List<FeedHeuristicCandidate> candidates = feedQueryRepository.findHeuristicCandidatesByCursor(
                    cursor,
                    memberId,
                    pageSize
            );

            List<Long> getBestEvents = selectBestEventByHeuristic(candidates, memberId);

            /*
            while(eventIds.size() < pageSize || !getBestEvents.isEmpty()) {
                eventIds.add(getBestEvents.index);
            }
             */

            //feedBuffer.push(candidates.stream().map());
        }

        return assemble(eventIds);
    }

    public CursorResponse<GetFeedPostListDto> getCursorResponse(
            List<GetFeedPostListDto> feedPostListDto,
            int pageSize
            ) {


        return null;
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

        return List.of();
    }
}
