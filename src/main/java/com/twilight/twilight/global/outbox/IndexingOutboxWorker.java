package com.twilight.twilight.global.outbox;

import com.twilight.twilight.domain.bulletin.search.coordinator.SearchSyncHandlerRegistry;
import com.twilight.twilight.domain.bulletin.search.handler.SearchSyncHandler;
import com.twilight.twilight.global.outbox.repository.IndexingOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IndexingOutboxWorker {

    private final IndexingOutboxRepository indexingOutboxRepository;
    private final SearchSyncHandler searchSyncHandler;
    private final SearchSyncHandlerRegistry handlerRegistry;

    @Transactional
    public List<IndexingOutbox> pollAndMarkProcessing(int batchSize) {

        return List.of();
    }

    public void process(IndexingOutbox event) {
        handlerRegistry
                .get(event.getAggregateType())
                .process(event);
    }

}
