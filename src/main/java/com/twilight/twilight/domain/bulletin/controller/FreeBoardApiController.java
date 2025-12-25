package com.twilight.twilight.domain.bulletin.controller;

import com.twilight.twilight.domain.bulletin.dto.*;
import com.twilight.twilight.domain.bulletin.service.FreeBoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bulletin/free-board")
public class FreeBoardApiController {

    private final FreeBoardPostService freeBoardPostService;

    /*
    @GetMapping("{post-id}")
    public ResponseEntity<List<GetFreeBoardPostReplyDto>>  getFreeBoardPost(
            @PathVariable(name = "post-id") Long postId) {

        return ResponseEntity.ok(freeBoardPostService.getFreeBoardPostReplies(postId));
    }
            */

    @GetMapping()
    public CursorResponse<GetFreeBoardPostListDto> getFreeBoardPosts(
            PageCursorRequest pageRequest
    ) {
        int pageSize = pageRequest.pageSizeOrDefault();
        /*
        Cursor cursor = pageRequest.toCursor();
        Long lastId = Optional.ofNullable(cursor)
                .map(Cursor::lastId)
                .orElse(null);
                */

        List<GetFreeBoardPostListDto> postLists = freeBoardPostService.getPostsByCursor(pageRequest);

        return freeBoardPostService.getCursorResponse(postLists, pageSize);
    }

    @GetMapping("/{post-id}/{reply-id}")
    public CursorResponse<GetFreeBoardPostReplyDto> getFreeBoardChildReplies(
            PageCursorRequest pageCursorRequest,
            @PathVariable("post-id") Long postId,
            @PathVariable("reply-id") Long replyId
    ) {
        Cursor cursor = pageCursorRequest.toCursor();
        int pageSize = pageCursorRequest.pageSizeOrDefault();

        List<GetFreeBoardPostReplyDto> replyLists = freeBoardPostService.getChildrenRepliesByCursor(
                pageCursorRequest,
                postId,
                replyId
                );

        return freeBoardPostService.getReplyCursorResponse(replyLists, pageSize);
    }

}
