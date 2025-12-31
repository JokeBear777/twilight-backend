package com.twilight.twilight.global.outbox;

import com.twilight.twilight.global.outbox.Type.AggregateType;
import com.twilight.twilight.global.outbox.Type.OutboxEventType;
import com.twilight.twilight.global.outbox.Type.OutboxStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class IndexingOutboxRowMapper implements RowMapper<IndexingOutbox> {

    @Override
    public IndexingOutbox mapRow(ResultSet rs, int rowNum) throws SQLException {

        return IndexingOutbox.builder()
                .outboxId(rs.getLong("outbox_id"))
                .aggregateType(
                        AggregateType.valueOf(rs.getString("aggregate_type"))
                )
                .aggregateId(rs.getLong("aggregate_id"))
                .eventType(
                        OutboxEventType.valueOf(rs.getString("event_type"))
                )
                .status(
                        OutboxStatus.valueOf(rs.getString("status"))
                )
                .retryCount(rs.getInt("retry_count"))
                .nextRetryAt(
                        rs.getTimestamp("next_retry_at") != null
                                ? rs.getTimestamp("next_retry_at").toLocalDateTime()
                                : null
                )
                .createdAt(
                        rs.getTimestamp("created_at").toLocalDateTime()
                )
                .updatedAt(
                        rs.getTimestamp("updated_at") != null
                                ? rs.getTimestamp("updated_at").toLocalDateTime()
                                : null
                )
                .build();
    }


}
