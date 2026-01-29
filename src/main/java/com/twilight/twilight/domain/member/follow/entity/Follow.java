package com.twilight.twilight.domain.member.follow.entity;

import com.twilight.twilight.domain.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "follow",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_follow",
                columnNames = {"follower_id", "following_id"}
        ),
        indexes = {
                @Index(name = "idx_following", columnList = "following_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    // 나(팔로우 하는 사람)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private Member follower;

    // 상대(내가 팔로우 당하는 사람)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private Member following;


    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Follow create(Member follower, Member following) {
        if (follower == null || following == null) {
            throw new IllegalArgumentException("follower/following 은 null 이면 안됩니다.");
        }
        if (follower.getMemberId().equals(following.getMemberId())) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        return new Follow(follower, following);
    }

    private Follow(Member follower, Member following) {
        this.follower = follower;
        this.following = following;
    }

}
