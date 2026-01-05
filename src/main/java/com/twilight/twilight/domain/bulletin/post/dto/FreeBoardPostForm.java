package com.twilight.twilight.domain.bulletin.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardPostForm {
    private String title;
    private String content;
    private Long parentReplyId;
}
