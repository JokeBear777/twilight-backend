package com.twilight.twilight.domain.member.follow.controller;

import com.twilight.twilight.domain.member.follow.dto.FollowCountResponse;
import com.twilight.twilight.domain.member.follow.dto.FollowStatusResponse;
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

    @DeleteMapping("/follows/{targetMemberId}")
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

    @GetMapping("{memberId}/followers")
    public CursorResponse<GetFollowListDto> getFollowers(
            @PathVariable Long memberId,
            FollowCursorRequest pageRequest
    ) {
        List<GetFollowListDto> followListDtoList = followService.getFollowersByCursor(pageRequest, memberId);

        return followService.getCursorResponse(followListDtoList, pageRequest.pageSizeOrDefault());
    }

    @GetMapping("me/followers")
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

    @GetMapping("{memberId}/followings")
    public CursorResponse<GetFollowListDto> getFollowings(
            @PathVariable Long memberId,
            FollowCursorRequest pageRequest
    ) {
        List<GetFollowListDto> followingListDtoList = followService.getFollowingsByCursor(
                pageRequest,
                memberId
        );

        return followService.getCursorResponse(followingListDtoList, pageRequest.pageSizeOrDefault());
    }

    @GetMapping("me/followings")
    public CursorResponse<GetFollowListDto> getMyFollowings(
            CustomUserDetails customUserDetails,
            FollowCursorRequest pageRequest
    ) {

        List<GetFollowListDto> followingListDtoList = followService.getFollowingsByCursor(
                pageRequest,
                customUserDetails.getMember().getMemberId()
        );

        return followService.getCursorResponse(followingListDtoList, pageRequest.pageSizeOrDefault());
    }

    @GetMapping("{targetMemberId}/exists")
    public ResponseEntity<?> getFollowStatus(
            @PathVariable Long targetMemberId,
            CustomUserDetails customUserDetails
    ) {
        FollowStatusResponse response = followService.getFollowStatus(
                targetMemberId, customUserDetails.getMember().getMemberId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("{targetMemberId}/follow-count")
    public ResponseEntity<?> getTargetMemberFollowCount(
            @PathVariable Long targetMemberId
    ) {
        FollowCountResponse response = followService.getFollowCount(targetMemberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{me/follow-count")
    public ResponseEntity<?> getTargetMemberFollowCount(
            CustomUserDetails customUserDetails
    ) {
        FollowCountResponse response = followService.getFollowCount(customUserDetails.getMember().getMemberId());
        return ResponseEntity.ok(response);
    }




}
