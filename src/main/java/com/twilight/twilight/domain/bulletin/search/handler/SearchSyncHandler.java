package com.twilight.twilight.domain.bulletin.search.handler;

import com.twilight.twilight.global.outbox.IndexingOutbox;
import com.twilight.twilight.global.outbox.Type.AggregateType;

public interface SearchSyncHandler {
    AggregateType supports();
    void process(IndexingOutbox event);
}
