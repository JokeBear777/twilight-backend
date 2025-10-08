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
@Table(name = "free_board_post")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FreeBoardPost {

    @Id
    @Column(name = "free_board_post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long freeBoardPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "views", nullable = false)
    @Builder.Default
    private int views = 0;

    @Column(name = "number_of_recommendations", nullable = false)
    @Builder.Default
    private int numberOfRecommendations = 0;

    @Column(name = "number_of_comments", nullable = false)
    @Builder.Default
    private int numberOfComments = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
