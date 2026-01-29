package com.twilight.twilight.domain.member.member.dto;

import com.twilight.twilight.domain.member.member.entity.Member;

public record TargetMemberInfoResponse (
        String memberName
) {

    public static TargetMemberInfoResponse from (Member member) {
        return new TargetMemberInfoResponse(member.getMemberName());
    }
}
