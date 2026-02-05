package com.twilight.twilight.domain.bulletin.feed.repository;

import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursor;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursorRequest;
import com.twilight.twilight.domain.bulletin.feed.heuristic.FeedHeuristicCandidate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedQueryRepositoryImpl implements FeedQueryRepository {

    @Override
    public List<FeedHeuristicCandidate> findHeuristicCandidatesByCursor(
            FeedCursor cursor,
            Long memberId,
            int pageSize) {


        return List.of();
    }

}
