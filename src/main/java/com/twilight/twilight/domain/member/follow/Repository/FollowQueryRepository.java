package com.twilight.twilight.domain.member.follow.Repository;

import com.twilight.twilight.domain.member.follow.dto.FollowCursor;
import com.twilight.twilight.domain.member.follow.dto.GetFollowListDto;

import java.util.List;

public interface FollowQueryRepository {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    List<GetFollowListDto> findFollowerListByCursor(FollowCursor cursor, int pageSize, Long targetMemberId);
}
