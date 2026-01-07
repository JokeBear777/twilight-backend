package com.twilight.twilight.domain.bulletin.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFreeBoardPostListDto {

    private Long freeBoardPostId;

    private Long memberId;

    private String title;

    private String content; //일부만 보낼까나, 어짜피 리스트니까
    
    private String name; //추후 닉네임으로 변경할수도

    private int views;

    private int numberOfRecommendations;

    private int numberOfComments;

    private LocalDateTime createdAt;

    public GetFreeBoardPostListDto(
            Long freeBoardPostId,
            Long memberId,
            String title,
            String content,
            String name,
            int views,
            int numberOfRecommendations,
            int numberOfComments,
            LocalDateTime createdAt
    ) {
        this.freeBoardPostId = freeBoardPostId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.name = name;
        this.views = views;
        this.numberOfRecommendations = numberOfRecommendations;
        this.numberOfComments = numberOfComments;
        this.createdAt = createdAt;
    }

}
