package com.twilight.twilight.domain.bulletin.search.handler;

import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;
import com.twilight.twilight.domain.bulletin.post.repository.FreeBoardPostRepository;
import com.twilight.twilight.global.outbox.IndexingOutbox;
import com.twilight.twilight.global.outbox.Type.AggregateType;
import com.twilight.twilight.global.search.NgramIndexService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FreeBoardSearchSyncHandler implements SearchSyncHandler{

    private final FreeBoardPostRepository freeBoardPostRepository;
    private final NgramIndexService ngramIndexService;

    @Override
    public AggregateType supports() {
        return AggregateType.FREE_BOARD_POST;
    }

    @Override
    public void process(IndexingOutbox event) {
        Long postId = event.getAggregateId();

        switch (event.getEventType()) {
            case UPSERT -> handleUpsert(postId);
            case DELETE -> handleDelete(postId);
        }
    }

    private void handleUpsert(Long postId) {
        freeBoardPostRepository.findById(postId)
                .filter(post -> !post.isDeleted())
                .ifPresentOrElse(
                        post -> ngramIndexService.reindexFreeBoardPost(post),
                        () -> ngramIndexService.deleteByPostId(postId)
                );
    }

    private void handleDelete(Long postId) {
        ngramIndexService.deleteByPostId(postId);
    }
}
