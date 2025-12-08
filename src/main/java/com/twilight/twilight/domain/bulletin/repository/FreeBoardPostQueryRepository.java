package com.twilight.twilight.domain.bulletin.repository;

import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostReplyDto;

import java.util.List;

public interface FreeBoardPostQueryRepository {
    List<GetFreeBoardPostListDto> findTopNByOrderByCreatedAtDesc(int number);
    List<GetFreeBoardPostReplyDto> findTopNParentRepliesOrderByCreatedAtDesc(Long postId, int count);
    List<GetFreeBoardPostReplyDto> findLatestChildReplyByReplyId(Long replyId, int count);
    List<GetFreeBoardPostReplyDto> findAllChildReplyByReplyId(Long replyId);
    List<GetFreeBoardPostReplyDto> findChildrenByParentIds(List<Long> parentIds);
    long countParentRepliesByPostId(Long postId);
    public List<GetFreeBoardPostReplyDto> findParentRepliesOrderByCreatedAtAsc(Long postId, Long page, int size);
}
