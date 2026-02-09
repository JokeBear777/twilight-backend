package com.twilight.twilight.domain.bulletin.feed.repository;

import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursor;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursorRequest;
import com.twilight.twilight.domain.bulletin.feed.heuristic.FeedHeuristicCandidate;

import java.util.List;

public interface FeedQueryRepository {
    List<FeedHeuristicCandidate> findHeuristicCandidatesByCursor(FeedCursor cursor, Long memberId, int pageSize);
}
