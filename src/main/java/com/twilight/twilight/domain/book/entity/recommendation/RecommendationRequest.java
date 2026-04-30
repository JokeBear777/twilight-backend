package com.twilight.twilight.domain.book.entity.recommendation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_request")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RecommendationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private RecommendationRequestStatus status;

    @Builder.Default
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    @Lob
    @Column(name = "error_message", columnDefinition = "LONGTEXT")
    private String errorMessage;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void markProcessing() {
        this.status = RecommendationRequestStatus.PROCESSING;
        this.errorMessage = null;
    }

    public void markSuccess() {
        this.status = RecommendationRequestStatus.SUCCESS;
        this.completedAt = LocalDateTime.now();
        this.errorMessage = null;
    }

    public void markFailed(String errorMessage) {
        this.status = RecommendationRequestStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    public void markDlq(String errorMessage) {
        this.status = RecommendationRequestStatus.DLQ;
        this.errorMessage = errorMessage;
    }

    public void increaseRetryCount() {
        this.retryCount++;
    }

    public boolean isSuccess() {
        return RecommendationRequestStatus.SUCCESS.equals(this.status);
    }

    public boolean isDlq() {
        return RecommendationRequestStatus.DLQ.equals(this.status);
    }
}
