package com.twilight.twilight.domain.bulletin.dto;

import com.twilight.twilight.domain.bulletin.entity.FreeBoardPostReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFreeBoardPostReplyDto {

    private Long freeBoardPostReplyId;

    private Long parentReplyId;

    private String replyWriterName;

    private Long memberId; //작성자

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<GetFreeBoardPostReplyDto> childrenList;

    private boolean hasMoreChildren;

    public static GetFreeBoardPostReplyDto fromEntity(FreeBoardPostReply reply) {
        return GetFreeBoardPostReplyDto.builder()
                .freeBoardPostReplyId(reply.getFreeBoardPostReplyId())
                .parentReplyId(
                        reply.getParentReply() != null
                                ? reply.getParentReply().getFreeBoardPostReplyId()
                                : null)
                .replyWriterName(reply.getMember().getMemberName())
                .memberId(reply.getMember().getMemberId())
                .content(reply.getContent())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .childrenList(List.of())
                .hasMoreChildren(false)
                .build();
    }

    //QueryDsl constructor projection 용 -> children 필드 기본값 처리
    public GetFreeBoardPostReplyDto(
            Long freeBoardPostReplyId,
            Long parentReplyId,
            String replyWriterName,
            Long memberId,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.freeBoardPostReplyId = freeBoardPostReplyId;
        this.parentReplyId = parentReplyId;
        this.replyWriterName = replyWriterName;
        this.memberId = memberId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.childrenList = List.of();
        this.hasMoreChildren = false;
    }

}
