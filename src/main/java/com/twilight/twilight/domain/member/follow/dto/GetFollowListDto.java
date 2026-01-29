package com.twilight.twilight.domain.member.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetFollowListDto {

    private Long memberId;
    private String name;
    private String profileImageUrl;
}

