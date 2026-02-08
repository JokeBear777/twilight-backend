package com.twilight.twilight.domain.bulletin.feed.heuristic;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class RelationScoreCalculator {

    public List<Long> relationScoreCalculate(List<FeedHeuristicCandidate> candidates, Map<Long, Integer> relationScoreMap) {


        return candidates.stream()
                .sorted(Comparator.comparingInt( (FeedHeuristicCandidate c) -> {
                    //int baseScore = c.getBaseScore(); // 조회수, 좋아요 등
                    int relationScore =
                            relationScoreMap.getOrDefault(c.targetId(), 0);

                    return relationScore;
                }).reversed())
                .map(FeedHeuristicCandidate::feedEventId)
                .toList();
    }
}
