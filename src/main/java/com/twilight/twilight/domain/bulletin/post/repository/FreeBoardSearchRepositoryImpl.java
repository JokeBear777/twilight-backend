package com.twilight.twilight.domain.bulletin.post.repository;

import com.twilight.twilight.domain.bulletin.post.dto.Cursor;
import com.twilight.twilight.domain.bulletin.post.dto.GetFreeBoardPostListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeBoardSearchRepositoryImpl implements FreeBoardSearchRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<GetFreeBoardPostListDto> findFirstSearchPosts (List<String> ngrams, int threshold, int size) {
        if (ngrams.isEmpty()) {
            return List.of();
        }

        String sql = """
        SELECT
            p.free_board_post_id,
            m.member_id,
            p.title,
            p.content,
            m.member_name AS name,
            p.views,
            p.number_of_recommendations,
            p.number_of_comments,
            p.created_at
        FROM free_board_post p
        JOIN (
            SELECT ni.post_id
            FROM free_board_ngram_posting ni
            WHERE ni.ngram IN (:ngrams)
            GROUP BY ni.post_id
            HAVING COUNT(*) >= :threshold
        ) s ON s.post_id = p.free_board_post_id
        JOIN member_info m ON m.member_id = p.member_id
        ORDER BY
            p.created_at DESC,
            p.free_board_post_id DESC
        LIMIT :limit
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ngrams", ngrams)
                .addValue("threshold", threshold)
                .addValue("limit", size );

        return namedParameterJdbcTemplate.query(
                sql,
                params,
                rowMapper()
        );
    }

    public List<GetFreeBoardPostListDto> findSearchPostsByCursor(List<String> ngrams, int threshold, Cursor cursor, int size) {
        if (ngrams.isEmpty()) {
            return List.of();
        }

        String sql = """
        SELECT
            p.free_board_post_id,
            m.member_id,
            p.title,
            p.content,
            m.member_name AS name,
            p.views,
            p.number_of_recommendations,
            p.number_of_comments,
            p.created_at
        FROM free_board_post p
        JOIN (
            SELECT ni.post_id
            FROM free_board_ngram_posting ni
            WHERE ni.ngram IN (:ngrams)
            GROUP BY ni.post_id
            HAVING COUNT(*) >= :threshold
        ) s ON s.post_id = p.free_board_post_id
        JOIN member_info m ON m.member_id = p.member_id
        WHERE
            (p.created_at, p.free_board_post_id) < (:createdAt, :lastId)
        ORDER BY
            p.created_at DESC,
            p.free_board_post_id DESC
        LIMIT :limit
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ngrams", ngrams)
                .addValue("threshold", threshold)
                .addValue("createdAt", cursor.lastCreatedAt())
                .addValue("lastId", cursor.lastId())
                .addValue("limit", size);

        return namedParameterJdbcTemplate.query(
                sql,
                params,
                rowMapper()
        );
    }


    private RowMapper<GetFreeBoardPostListDto> rowMapper() {
        return (rs, rowNum) -> new GetFreeBoardPostListDto(
                rs.getLong("free_board_post_id"),
                rs.getLong("member_id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getString("name"),
                rs.getInt("views"),
                rs.getInt("number_of_recommendations"),
                rs.getInt("number_of_comments"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
