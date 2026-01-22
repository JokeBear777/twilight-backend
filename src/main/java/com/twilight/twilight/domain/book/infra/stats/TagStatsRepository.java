package com.twilight.twilight.domain.book.infra.stats;

import org.springframework.stereotype.Repository;

import java.util.List;


public interface TagStatsRepository {
    List<Long> getSortedTagIdList(List<Long> tagIdList);
}
