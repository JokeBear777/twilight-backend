package com.twilight.twilight.domain.bulletin.post.repository;

import com.twilight.twilight.domain.bulletin.post.dto.PostCursor;
import com.twilight.twilight.domain.bulletin.post.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.post.dto.GetFreeBoardPostReplyDto;

import java.util.List;

public interface FreeBoardPostQueryRepository {
    List<GetFreeBoardPostListDto> findTopNByOrderByCreatedAtDesc(int number);
    List<GetFreeBoardPostReplyDto> findTopNParentRepliesOrderByCreatedAtDesc(Long postId, int count);
    List<GetFreeBoardPostReplyDto> findLatestChildReplyByReplyId(Long replyId, int count);
    List<GetFreeBoardPostReplyDto> findAllChildReplyByReplyId(Long replyId);
    List<GetFreeBoardPostReplyDto> findChildrenByParentIds(List<Long> parentIds);
    long countParentRepliesByPostId(Long postId);
    List<GetFreeBoardPostReplyDto> findParentRepliesOrderByCreatedAtAsc(Long postId, Long page, int size);
    List<GetFreeBoardPostListDto> findPostsByCursor(PostCursor postCursor, int size);
    List<GetFreeBoardPostReplyDto> findChildReplyByCursor(PostCursor postCursor, int size, Long postId, Long parentId);
}
