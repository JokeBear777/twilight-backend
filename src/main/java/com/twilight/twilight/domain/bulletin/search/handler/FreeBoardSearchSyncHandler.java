package com.twilight.twilight.domain.bulletin.search.handler;

import com.twilight.twilight.domain.bulletin.post.repository.FreeBoardPostRepository;
import com.twilight.twilight.global.outbox.IndexingOutbox;
import com.twilight.twilight.global.outbox.Type.AggregateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FreeBoardSearchSyncHandler implements SearchSyncHandler{

    private final FreeBoardPostRepository freeBoardPostRepository;

    @Override
    public AggregateType supports() {
        return AggregateType.FREE_BOARD_POST;
    }

    @Override
    public void process(IndexingOutbox event) {

    }
}
