package com.twilight.twilight.domain.bulletin.feed.stat;

public interface AuthorRelationSignalAccumulator {
    /**
     * 관계 신호를 누적한다.
     * @return true면 관계를 DB로 승격해야 함
     */
    boolean accumulateViewSignal(Long viewerId, Long authorId);
}
