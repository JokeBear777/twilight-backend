package com.twilight.twilight.domain.book.repository.recommendation;

import com.twilight.twilight.domain.book.entity.recommendation.RecommendationRequest;
import com.twilight.twilight.domain.book.entity.recommendation.RecommendationRequestStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecommendationRequestRepository extends JpaRepository<RecommendationRequest, Long> {

    Optional<RecommendationRequest> findTopByMemberIdOrderByCreatedAtDesc(Long memberId);

    Optional<RecommendationRequest> findTopByMemberIdAndStatusOrderByCreatedAtDesc(
            Long memberId,
            RecommendationRequestStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RecommendationRequest r where r.id = :id")
    Optional<RecommendationRequest> findByIdForUpdate(@Param("id") Long id);
}
