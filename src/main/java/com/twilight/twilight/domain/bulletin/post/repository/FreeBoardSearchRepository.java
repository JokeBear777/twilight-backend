package com.twilight.twilight.domain.bulletin.post.repository;

import com.twilight.twilight.domain.bulletin.post.dto.Cursor;
import com.twilight.twilight.domain.bulletin.post.dto.GetFreeBoardPostListDto;

import java.util.List;

public interface FreeBoardSearchRepository {
    List<GetFreeBoardPostListDto> findSearchPostsByCursor(List<String> ngrams, int threshold, Cursor cursor, int size);
    List<GetFreeBoardPostListDto> findFirstSearchPosts (List<String> ngrams, int threshold, int size);
}
