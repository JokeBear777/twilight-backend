package com.twilight.twilight.domain.bulletin.repository;

import com.twilight.twilight.domain.bulletin.entity.FreeBoardPostReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardPostReplyRepository extends JpaRepository<FreeBoardPostReply, Long> {
    List<FreeBoardPostReply> findByFreeBoardPost_FreeBoardPostId(Long freeBoardPostId);
    void deleteByFreeBoardPost_FreeBoardPostId(Long freeBoardPostId);
}
