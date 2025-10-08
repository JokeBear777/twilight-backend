package com.twilight.twilight.domain.bulletin.repository;

import com.twilight.twilight.domain.bulletin.entity.FreeBoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreeBoardPostRepository extends JpaRepository<FreeBoardPost, Long> {
    Optional<FreeBoardPost> findByFreeBoardPostId(Long postId);
}
