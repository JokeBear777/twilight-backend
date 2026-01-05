package com.twilight.twilight.global.outbox;

import com.twilight.twilight.global.outbox.Type.AggregateType;
import com.twilight.twilight.global.outbox.Type.OutboxEventType;
import com.twilight.twilight.global.outbox.Type.OutboxStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexingOutbox {

    private Long outboxId;

    private AggregateType aggregateType;
    private Long aggregateId;

    private OutboxEventType eventType;
    private OutboxStatus status;

    private int retryCount;
    private LocalDateTime nextRetryAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    private IndexingOutbox(
            Long outboxId,
            AggregateType aggregateType,
            Long aggregateId,
            OutboxEventType eventType,
            OutboxStatus status,
            int retryCount,
            LocalDateTime nextRetryAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.outboxId = outboxId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.status = status;
        this.retryCount = retryCount;
        this.nextRetryAt = nextRetryAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static IndexingOutbox create(
            AggregateType aggregateType,
            Long aggregateId,
            OutboxEventType eventType,
            LocalDateTime now
    ) {
        return IndexingOutbox.builder()
                .aggregateType(aggregateType)
                .aggregateId(aggregateId)
                .eventType(eventType)
                .status(OutboxStatus.PENDING)
                .retryCount(0)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
