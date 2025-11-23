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
public class GetFreeBoardPostEditDto {

    private Long postId;

    private String title;

    private String content;

    private String name; //추후 닉네임으로 변경할수도

    private int views;

    private LocalDateTime createdAt;

    public static GetFreeBoardPostEditDto fromEntity(FreeBoardPost freeBoardPost) {
        return GetFreeBoardPostEditDto.builder()
                .postId(freeBoardPost.getFreeBoardPostId())
                .title(freeBoardPost.getTitle())
                .content(freeBoardPost.getContent())
                .name(freeBoardPost.getMember().getMemberName())
                .views(freeBoardPost.getViews())
                .createdAt(freeBoardPost.getCreatedAt())
                .build();
    }
}
