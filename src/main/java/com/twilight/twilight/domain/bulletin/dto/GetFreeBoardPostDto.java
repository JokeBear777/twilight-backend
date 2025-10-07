package com.twilight.twilight.domain.bulletin.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFreeBoardPostDto {

    private Long freeBoardPostId;

    private Long memberId;

    private String content;

    private int totalPostCount; //이거 기반으로 뷰에서 1/2/3.....이런식으로 게시판 페이지 생성

    private int views;

    private int numberOfRecommendations = 0;

    private int numberOfComments = 0;

    private LocalDateTime createdAt;

}
