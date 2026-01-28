package com.twilight.twilight.domain.member.follow.Repository;

import com.twilight.twilight.domain.member.follow.dto.FollowCountResponse;
import com.twilight.twilight.domain.member.follow.dto.FollowCursor;
import com.twilight.twilight.domain.member.follow.dto.FollowStatusResponse;
import com.twilight.twilight.domain.member.follow.dto.GetFollowListDto;

import java.util.List;

public interface FollowQueryRepository {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    List<GetFollowListDto> findFollowerListByCursor(FollowCursor cursor, int pageSize, Long targetMemberId);
    List<GetFollowListDto> findFollowingListByCursor(FollowCursor cursor, int pageSize, Long targetMemberId);
    FollowStatusResponse getFollowStatus(Long targetMemberId, Long meId);
    FollowCountResponse getFollowCount(Long targetMemberId);
}
