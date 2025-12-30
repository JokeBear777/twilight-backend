package com.twilight.twilight.domain.bulletin.post.repository;

import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPost;
import com.twilight.twilight.domain.bulletin.post.entity.FreeBoardPostRecommendation;
import com.twilight.twilight.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardPostRecommendationRepository extends JpaRepository<FreeBoardPostRecommendation, Long> {

    public Boolean existsByMemberAndPost(Member member, FreeBoardPost post);
}
