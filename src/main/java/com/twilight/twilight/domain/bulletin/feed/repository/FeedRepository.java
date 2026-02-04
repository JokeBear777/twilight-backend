package com.twilight.twilight.domain.bulletin.feed.repository;

import com.twilight.twilight.domain.bulletin.feed.entity.FeedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<FeedEvent, Long> {
}
