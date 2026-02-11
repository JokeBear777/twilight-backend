package com.twilight.twilight.domain.bulletin.feed.dto;

import com.twilight.twilight.domain.bulletin.feed.type.FeedEventType;
import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;

import java.time.LocalDateTime;
import java.util.List;

public record GetFeedPostListDto (

        // 게시글
        Long postId,
        String content,
        LocalDateTime createdAt,
        String eventType,

        // 작성자
        Long authorId,
        String authorName,
        String authorProfileImageUrl,

        // 인터랙션
        int likeCount,
        int commentCount
        //boolean likedByMe

) {
    static public GetFeedPostListDto fromFreeBoardPost(FreeBoardPost freeBoardPost) {
        return new GetFeedPostListDto(
                freeBoardPost.getFreeBoardPostId(),
                freeBoardPost.getContent(),
                freeBoardPost.getCreatedAt(),
                FeedEventType.FREE_BOARD_POST_CREATED.toString(),
                freeBoardPost.getMember().getMemberId(),
                freeBoardPost.getMember().getMemberName(),
                "https://mond-al.github.io/assets/images/forTest/ratio/all_ratio/image_3_320x240.png", //mock url 후에 이미지 기능 추가하면 수정
                freeBoardPost.getNumberOfRecommendations(),
                freeBoardPost.getNumberOfComments()
        );
    }

}
