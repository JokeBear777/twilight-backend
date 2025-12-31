package com.twilight.twilight.global.outbox.repository;

import com.twilight.twilight.global.outbox.IndexingOutbox;
import com.twilight.twilight.global.outbox.IndexingOutboxRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcIndexingOutboxRepository implements IndexingOutboxRepository {
    private final JdbcTemplate jdbcTemplate;
    private final IndexingOutboxRowMapper rowMapper;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public void save(IndexingOutbox outbox) {
        String sql = """
            INSERT INTO indexing_outbox
              (aggregate_type, aggregate_id, event_type, status,
               retry_count, next_retry_at, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                outbox.getAggregateType().name(),
                outbox.getAggregateId(),
                outbox.getEventType().name(),
                outbox.getStatus().name(),
                outbox.getRetryCount(),
                outbox.getNextRetryAt() != null
                        ? Timestamp.valueOf(outbox.getNextRetryAt())
                        : null,
                Timestamp.valueOf(outbox.getCreatedAt())
        );
    }

    @Override
    public List<IndexingOutbox> findPendingEvents(int limit, LocalDateTime now) {
        String sql = """
            SELECT *
            FROM indexing_outbox
            WHERE status = 'PENDING'
              AND (next_retry_at IS NULL OR next_retry_at <= ?)
            ORDER BY created_at
            LIMIT ?
            FOR UPDATE SKIP LOCKED
        """;

        return jdbcTemplate.query(
                sql,
                rowMapper,
                Timestamp.valueOf(now),
                limit
        );
    }

    @Override
    public void markProcessing(List<Long> outboxIds) {
        if (outboxIds.isEmpty()) return;

        String sql = """
        UPDATE indexing_outbox
        SET status = 'PROCESSING'
        WHERE outbox_id IN (:outboxIds)
    """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("outboxIds", outboxIds);

        namedJdbcTemplate.update(sql, params);
    }

    @Override
    public void markDone(Long outboxId) {
        jdbcTemplate.update(
                "UPDATE indexing_outbox SET status = 'DONE' WHERE outbox_id = ?",
                outboxId
        );
    }

    @Override
    public void markFailed(Long outboxId, int nextRetrySeconds) {
        jdbcTemplate.update("""
            UPDATE indexing_outbox
            SET status = 'FAILED',
                retry_count = retry_count + 1,
                next_retry_at = DATE_ADD(NOW(), INTERVAL ? SECOND)
            WHERE outbox_id = ?
        """, nextRetrySeconds, outboxId);
    }
}
