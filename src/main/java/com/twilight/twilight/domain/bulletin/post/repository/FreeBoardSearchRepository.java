package com.twilight.twilight.domain.bulletin.post.repository;

import com.twilight.twilight.domain.bulletin.post.dto.PostCursor;
import com.twilight.twilight.domain.bulletin.post.dto.GetFreeBoardPostListDto;

import java.util.List;

public interface FreeBoardSearchRepository {
    List<GetFreeBoardPostListDto> findSearchPostsByCursor(List<String> ngrams, int threshold, PostCursor postCursor, int size);
    List<GetFreeBoardPostListDto> findFirstSearchPosts (List<String> ngrams, int threshold, int size);
}
