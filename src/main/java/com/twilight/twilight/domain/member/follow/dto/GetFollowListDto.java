package com.twilight.twilight.domain.member.follow.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFollowListDto {

    private Long followerId;

    private String followerName;

    private String selfImageUrl;
}
