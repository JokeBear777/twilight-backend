package com.twilight.twilight.domain.member.follow.dto;

public record FollowCountResponse (
        int followersCount,
        int followingsCount
){}
