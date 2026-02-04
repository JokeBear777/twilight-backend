package com.twilight.twilight.domain.bulletin.feed.controller;

import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursorRequest;
import com.twilight.twilight.domain.bulletin.feed.dto.GetFeedPostListDto;
import com.twilight.twilight.domain.bulletin.feed.service.FeedService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import com.twilight.twilight.global.cursor.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedApiController {

    private final FeedService feedService;

    @GetMapping()
    public CursorResponse<GetFeedPostListDto> getFeedPostList(
            FeedCursorRequest feedRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        int pageSize = feedRequest.pageSizeOrDefault();

        List<GetFeedPostListDto> listDtos = feedService.getFeedByCursor(
                feedRequest,
                customUserDetails.getMember().getMemberId());

        return feedService.getCursorResponse(listDtos, pageSize);
    }


}
