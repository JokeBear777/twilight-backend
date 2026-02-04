package com.twilight.twilight.domain.bulletin.feed.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GetFeedPostListDto (

        // 게시글
        Long postId,
        String content,
        LocalDateTime createdAt,

        // 작성자
        Long authorId,
        String authorName,
        String authorProfileImageUrl,

        // 미디어
        List<String> imageUrls,

        // 인터랙션
        int likeCount,
        int commentCount,
        boolean likedByMe

) {}
