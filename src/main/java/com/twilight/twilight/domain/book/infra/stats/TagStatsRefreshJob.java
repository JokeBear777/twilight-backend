package com.twilight.twilight.domain.book.infra.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TagStatsRefreshJob {

    private final TagStatsJdbcDao tagStatsJdbcDao;

    @Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시
    public void run() {
        var rows = tagStatsJdbcDao.aggregateUsageCount();
        for (var row : rows) {
            tagStatsJdbcDao.upsert(row.tagId(), row.usageCount());
        }
    }
}
