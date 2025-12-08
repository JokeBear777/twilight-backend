package com.twilight.twilight.domain.bulletin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardPostReplyForm {
    private Long parentReplyId;
    private String content;
    private Long currentPage;
}
