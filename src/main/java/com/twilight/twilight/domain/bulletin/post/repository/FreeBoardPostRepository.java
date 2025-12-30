package com.twilight.twilight.domain.bulletin.post.repository;

import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreeBoardPostRepository extends JpaRepository<FreeBoardPost, Long> {
    Optional<FreeBoardPost> findByFreeBoardPostId(Long postId);
    long countByDeletedAtIsNull();
}
