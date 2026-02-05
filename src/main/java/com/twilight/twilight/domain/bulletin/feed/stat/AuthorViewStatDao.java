package com.twilight.twilight.domain.bulletin.feed.stat;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class AuthorViewStatDao {

    private final JdbcTemplate jdbcTemplate;

    public void increaseViewCount(Long viewerId, Long authorId) {
        String sql = """
            INSERT INTO author_view_stat (viewer_id, author_id, view_count, last_viewed_at)
            VALUES (?, ?, 1, NOW())
            ON DUPLICATE KEY UPDATE
                view_count = view_count + 1,
                last_viewed_at = NOW()
        """;

        jdbcTemplate.update(sql, viewerId, authorId);
    }

    public Map<Long, Integer> findRelationScores(
            Long viewerId,
            List<Long> authorIds
    ) {
        if (authorIds == null || authorIds.isEmpty()) {
            return Map.of();
        }

        String inClause = authorIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = String.format("""
            SELECT author_id, view_count
            FROM author_view_stat
            WHERE viewer_id = ?
              AND author_id IN (%s)
        """, inClause);

        List<Object> params = new ArrayList<>();
        params.add(viewerId);
        params.addAll(authorIds);

        return jdbcTemplate.query(
                sql,
                params.toArray(),
                rs -> {
                    Map<Long, Integer> result = new HashMap<>();
                    while (rs.next()) {
                        result.put(
                                rs.getLong("author_id"),
                                rs.getInt("view_count")
                        );
                    }
                    return result;
                }
        );
    }

}
