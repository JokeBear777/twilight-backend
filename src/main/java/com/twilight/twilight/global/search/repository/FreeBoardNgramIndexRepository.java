package com.twilight.twilight.global.search.repository;

import com.twilight.twilight.global.search.NgramIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeBoardNgramIndexRepository implements NgramIndexRepository {

    private final JdbcTemplate jdbcTemplate;



    @Override
    public void bulkInert(List<NgramIndex> ngramIndexList) {


        String sql = """
        INSERT INTO free_board_ngram_posting (ngram, post_id)
        VALUES (?, ?)
    """;

        jdbcTemplate.batchUpdate(
                sql,
                ngramIndexList,
                1000, // batch size
                (ps, ngram) -> {
                    ps.setString(1, ngram.getNgram());
                    ps.setLong(2, ngram.getPostId());
                }
        );

    }

    @Override
    public void deleteByPostId(long postId) {
        jdbcTemplate.update(
                "DELETE FROM free_board_ngram_posting WHERE post_id = ?",
                postId
        );
    }
}
