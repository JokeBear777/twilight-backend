package com.twilight.twilight.domain.bulletin.dto;

import com.twilight.twilight.domain.bulletin.entity.FreeBoardPostReply;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFreeBoardPostReplyDto {

    private Long freeBoardPostReplyId;

    private FreeBoardPostReply parentReply;

    private String replyWriterName;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
