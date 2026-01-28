package com.twilight.twilight.domain.member.follow.dto;

public record FollowCountResponse (
        Long followerCount,
        Long followingCount
){}
