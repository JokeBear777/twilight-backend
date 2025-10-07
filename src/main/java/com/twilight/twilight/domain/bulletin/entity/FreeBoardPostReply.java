package com.twilight.twilight.domain.bulletin.entity;


import com.twilight.twilight.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "free_board_post_reply")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FreeBoardPostReply {

    @Id
    @Column(name = "free_board_post_reply_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer freeBoardPostReplyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_board_post_id")
    private FreeBoardPost freeBoardPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_reply_id")
    private FreeBoardPostReply parentReply;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
