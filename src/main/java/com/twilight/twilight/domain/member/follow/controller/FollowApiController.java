package com.twilight.twilight.domain.member.follow.controller;

import com.twilight.twilight.global.cursor.CursorResponse;
import com.twilight.twilight.domain.member.follow.Service.FollowService;
import com.twilight.twilight.domain.member.follow.dto.FollowCursorRequest;
import com.twilight.twilight.domain.member.follow.dto.GetFollowListDto;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/social/api/v1")
public class FollowApiController {

    private final FollowService followService;

    @PostMapping("/follows/{targetMemberId}")
    public ResponseEntity<?> follow(
            @PathVariable Long targetMemberId,
            CustomUserDetails customUserDetails
    ) {
        followService.followTargetMember(
                targetMemberId,
                customUserDetails.getMember().getMemberId()
                );

        return ResponseEntity.ok("follow success");
    }

    @DeleteMapping("follows/{targetMemberId}")
    public ResponseEntity<?> unfollow(
            @PathVariable Long targetMemberId,
            CustomUserDetails customUserDetails
    ) {
        followService.unfollowTargetMember(
                targetMemberId,
                customUserDetails.getMember().getMemberId()
        );

        return ResponseEntity.ok("unfollow success");
    }

    @GetMapping("{memberId}/followers/")
    public CursorResponse<GetFollowListDto> getFollowers(
            @PathVariable Long memberId,
            FollowCursorRequest pageRequest
    ) {
        List<GetFollowListDto> followListDtoList = followService.getFollowersByCursor(pageRequest, memberId);

        return followService.getCursorResponse(followListDtoList, pageRequest.pageSizeOrDefault());
    }

    @GetMapping("me/followers/")
    public CursorResponse<?> getMyFollowers(
            CustomUserDetails customUserDetails,
            FollowCursorRequest pageRequest
    ) {
        List<GetFollowListDto> followListDtoList = followService.getFollowersByCursor(
                pageRequest,
                customUserDetails.getMember().getMemberId()
        );

        return followService.getCursorResponse(followListDtoList, pageRequest.pageSizeOrDefault());
    }




}
