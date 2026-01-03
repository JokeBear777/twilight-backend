package com.twilight.twilight.global.search;

import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;
import com.twilight.twilight.global.search.repository.NgramIndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NgramIndexService {

    private final NgramIndexRepository ngramIndexRepository;
    private final NgramGenerator ngramGenerator;

    public void reindexFreeBoardPost(FreeBoardPost post) {
        deleteByPostId(post.getFreeBoardPostId());

        List<NgramIndex> ngramIndexList = ngramGenerator.generate(
                post.getTitle(),
                post.getContent(),
                post.getFreeBoardPostId()
        );

        ngramIndexRepository.bulkInert(ngramIndexList);
    }

    public void deleteByPostId(long postId) {
        ngramIndexRepository.deleteByPostId(postId);
    }



}
