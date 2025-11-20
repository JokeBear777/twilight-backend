package com.twilight.twilight.domain.bulletin.dto;

import com.twilight.twilight.domain.bulletin.entity.FreeBoardPostReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFreeBoardPostReplyDto {

    private Long freeBoardPostReplyId;

    private Long parentReplyId;

    private String replyWriterName;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static GetFreeBoardPostReplyDto fromEntity(FreeBoardPostReply reply) {
        return GetFreeBoardPostReplyDto.builder()
                .freeBoardPostReplyId(reply.getFreeBoardPostReplyId())
                .parentReplyId(reply.getParentReply().getFreeBoardPostReplyId())
                .replyWriterName(reply.getMember().getMemberName())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }

}
