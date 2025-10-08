package com.twilight.twilight.domain.bulletin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
