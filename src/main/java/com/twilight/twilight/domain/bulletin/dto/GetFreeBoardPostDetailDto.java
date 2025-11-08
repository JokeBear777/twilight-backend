package com.twilight.twilight.domain.bulletin.dto;

import com.twilight.twilight.domain.bulletin.entity.FreeBoardPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFreeBoardPostDetailDto {

    private Long freeBoardPostId;

    private Long memberId;

    private String title;

    private String content;

    private String name; //추후 닉네임으로 변경할수도

    private int views;

    private int numberOfRecommendations;

    private int numberOfComments;

    private LocalDateTime createdAt;

    public static GetFreeBoardPostDetailDto fromEntity(FreeBoardPost post) {
        return GetFreeBoardPostDetailDto.builder()
                .freeBoardPostId(post.getFreeBoardPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .name(post.getMember().getMemberName())
                .views(post.getViews())
                .numberOfRecommendations(post.getNumberOfRecommendations())
                .numberOfComments(post.getNumberOfComments())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
