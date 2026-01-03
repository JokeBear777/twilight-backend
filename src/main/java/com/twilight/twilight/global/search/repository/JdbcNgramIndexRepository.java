package com.twilight.twilight.global.search.repository;

import com.twilight.twilight.global.search.NgramIndex;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcNgramIndexRepository implements NgramIndexRepository {



    @Override
    public void bulkInert(List<NgramIndex> ngramIndexList) {

    }

    @Override
    public void deleteByPostId(long postId) {

    }
}
