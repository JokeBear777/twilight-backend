package com.twilight.twilight.global.outbox.repository;

import com.twilight.twilight.global.outbox.IndexingOutbox;

import java.time.LocalDateTime;
import java.util.List;

public interface IndexingOutboxRepository {
    //트랜잭션 내부
    void save(IndexingOutbox outbox);

    //consumer 전용
    List<IndexingOutbox> findPendingEvents(
            int limit,
            LocalDateTime now
    );

    //상태 전이
    void markProcessing(List<Long> outboxIds);

    void markDone(Long outboxId);

    void markFailed(Long outboxId, int nextRetrySeconds);
}
