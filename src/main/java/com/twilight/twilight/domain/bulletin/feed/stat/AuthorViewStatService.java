package com.twilight.twilight.domain.bulletin.feed.stat;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class AuthorViewStatService {

    private final AuthorViewStatDao authorViewStatDao;
    
    //서비스 핵심흐름이 아니라 로그만 남김, 통계의 경우 굳이 예외를던져 서비스에 영향을 줄 필요가 없다
    public void increaseAuthorViewStat(Long viewerId, Long authorId) {
        if (viewerId == null || authorId == null) {
            return;
        }

        try {
            authorViewStatDao.increaseViewCount(viewerId, authorId);
        } catch (DataAccessException e) {
            log.warn(
                    "Failed to record author view stat. viewerId={}, authorId={}",
                    viewerId, authorId, e
            );
        }
    }

    public Map<Long, Integer> getRelationScore(
            Long viewerId,
            List<Long> authorIds
    ) {
        if (viewerId == null || authorIds == null) {
            throw new IllegalArgumentException("viewerId and authorIds cannot be null");
        }

        if (authorIds.isEmpty()) {
            return Map.of();
        }

        try {
            return authorViewStatDao.findRelationScores(viewerId, authorIds);
        } catch (DataAccessException e) {
            log.warn(
                    "Failed to load relation scores. viewerId={}, authorIds={}",
                    viewerId, authorIds, e
            );
            return Map.of(); // 관계 점수 없으면 전부 0점 처리
        }
    }


}
