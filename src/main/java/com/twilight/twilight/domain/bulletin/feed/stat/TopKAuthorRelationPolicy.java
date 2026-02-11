package com.twilight.twilight.domain.bulletin.feed.stat;

import com.twilight.twilight.infra.feed.stat.AuthorViewStatDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopKAuthorRelationPolicy implements AuthorRelationPolicy {

    private final AuthorViewStatDao authorViewStatDao;
    private static final int MAX_RELATIONS = 100;
    private static final int PRUNE_COUNT = 10;

    @Override
    public void enforce(Long viewerId) {
        int viewerRelationCount =
                authorViewStatDao.countViewerRelation(viewerId);

        if (viewerRelationCount > MAX_RELATIONS) {
            authorViewStatDao.pruneOldestRelations(viewerId, PRUNE_COUNT);
        }


    }
}
