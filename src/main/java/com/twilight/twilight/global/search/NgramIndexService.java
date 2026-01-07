package com.twilight.twilight.global.search;

import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;
import com.twilight.twilight.domain.bulletin.post.repository.FreeBoardPostRepository;
import com.twilight.twilight.global.search.repository.FreeBoardNgramIndexRepository;
import com.twilight.twilight.global.search.repository.NgramIndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NgramIndexService {

    private final FreeBoardNgramIndexRepository freeBoardNgramIndexRepository;
    private final NgramGenerator ngramGenerator;

    public void reindexFreeBoardPost(FreeBoardPost post) {
        deleteByPostId(post.getFreeBoardPostId());

        List<NgramIndex> ngramIndexList = ngramGenerator.generateByPost(
                post.getTitle(),
                post.getContent(),
                post.getFreeBoardPostId()
        );

        freeBoardNgramIndexRepository.bulkInert(ngramIndexList);
    }

    public void deleteByPostId(long postId) {
        freeBoardNgramIndexRepository.deleteByPostId(postId);
    }



}
