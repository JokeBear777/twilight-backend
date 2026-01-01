package com.twilight.twilight.global.outbox.service;

import com.twilight.twilight.global.outbox.IndexingOutbox;
import com.twilight.twilight.global.outbox.Type.AggregateType;
import com.twilight.twilight.global.outbox.Type.OutboxEventType;
import com.twilight.twilight.global.outbox.repository.IndexingOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FreeBoardSearchSyncPublisher {
    private final IndexingOutboxRepository indexingOutboxRepository;

    public void publishPostUpsert(Long postId) {
        indexingOutboxRepository.save(
                IndexingOutbox.create(
                        AggregateType.FREE_BOARD_POST,
                        postId,
                        OutboxEventType.UPSERT,
                        LocalDateTime.now()
                )
        );
    }

    public void publishPostDelete(Long postId) {
        indexingOutboxRepository.save(
                IndexingOutbox.create(
                        AggregateType.FREE_BOARD_POST,
                        postId,
                        OutboxEventType.DELETE,
                        LocalDateTime.now()
                )
        );
    }
}
