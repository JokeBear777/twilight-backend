package com.twilight.twilight.domain.bulletin.feed.entity;

import com.twilight.twilight.domain.bulletin.feed.type.FeedEventType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "feed_event",
        indexes = {
                @Index(name = "idx_feed_event_actor_created",
                        columnList = "actor_id, created_at, feed_event_id")
        }
)
public class FeedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_event_id")
    private Long id;

    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private FeedEventType eventType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected FeedEvent() {}

    public FeedEvent(
            Long actorId,
            FeedEventType eventType,
            Long targetId
    ) {
        this.actorId = actorId;
        this.eventType = eventType;
        this.targetId = targetId;
        this.createdAt = LocalDateTime.now();
    }
}
