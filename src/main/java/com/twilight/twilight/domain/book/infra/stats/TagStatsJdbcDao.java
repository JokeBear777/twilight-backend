package com.twilight.twilight.domain.book.infra.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagStatsJdbcDao {

    private final JdbcTemplate jdbcTemplate;

    public List<TagCountRow> aggregateUsageCount() {
        String sql = """
            SELECT tag_id, COUNT(*) AS usage_count
            FROM book_tags
            GROUP BY tag_id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new TagCountRow(
                        rs.getLong("tag_id"),
                        rs.getLong("usage_count")
                )
        );
    }

    public void upsert(Long tagId, Long usageCount) {
        String sql = """
            INSERT INTO tag_stats(tag_id, usage_count, updated_at)
            VALUES (?, ?, NOW())
            ON DUPLICATE KEY UPDATE
                usage_count = VALUES(usage_count),
                updated_at = NOW()
        """;
        jdbcTemplate.update(sql, tagId, usageCount);
    }

    public record TagCountRow(Long tagId, Long usageCount) {}
}
