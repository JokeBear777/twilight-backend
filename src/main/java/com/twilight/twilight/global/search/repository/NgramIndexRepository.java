package com.twilight.twilight.global.search.repository;


import com.twilight.twilight.global.search.NgramIndex;

import java.util.List;

public interface NgramIndexRepository {
    void deleteByPostId(long postId);
    void bulkInert(List<NgramIndex> ngramIndexList);
}
