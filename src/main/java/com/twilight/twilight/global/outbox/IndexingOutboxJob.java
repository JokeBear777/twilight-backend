package com.twilight.twilight.global.outbox;

import com.twilight.twilight.global.outbox.repository.IndexingOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IndexingOutboxJob {

    private final IndexingOutboxWorker worker;
    private final IndexingOutboxRepository outboxRepository;

    @Scheduled(fixedRate = 1000)
    public void run() {
        List<IndexingOutbox> events = worker.pollAndMarkProcessing(20);

        for (IndexingOutbox event : events) {
            try {
               worker.process(event);
               outboxRepository.markDone(event.getOutboxId());
            } catch (Exception e) {
                outboxRepository.markFailed(
                        event.getOutboxId(),
                        calculateNextRetry(event.getRetryCount())
                );
            }
        }

    }

    private int calculateNextRetry(int retryCount) {
        return switch (retryCount) {
            case 0 -> 5;
            case 1 -> 30;
            case 2 -> 300;
            default -> 1800;
        };
    }
}
