package com.twilight.twilight.domain.member.follow.Repository;

import com.twilight.twilight.domain.member.follow.entity.Follow;
import com.twilight.twilight.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    void deleteByFollowerAndFollowing(Member follower, Member following);
}
