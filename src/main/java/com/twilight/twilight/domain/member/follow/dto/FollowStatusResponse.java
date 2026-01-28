package com.twilight.twilight.domain.member.follow.dto;

public record FollowStatusResponse (
        boolean following,   // 내가 팔로우
        boolean follower     // 나를 팔로우
) { }