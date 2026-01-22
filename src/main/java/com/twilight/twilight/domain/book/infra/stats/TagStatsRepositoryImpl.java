package com.twilight.twilight.domain.book.infra.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagStatsRepositoryImpl implements TagStatsRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> getSortedTagIdList(List<Long> tagIdList) {
        if (tagIdList == null || tagIdList.isEmpty()) {
            return List.of();
        }

        String placeholders = String.join(",", java.util.Collections.nCopies(tagIdList.size(), "?"));

        String sql = """
        SELECT tag_id
        FROM tag_stats
        WHERE tag_id IN (%s)
        ORDER BY usage_count ASC
    """.formatted(placeholders);

        return jdbcTemplate.query(
                sql,
                tagIdList.toArray(),
                (rs, rowNum) -> rs.getLong("tag_id")
        );
    }
}
