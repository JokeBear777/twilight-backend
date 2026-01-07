package com.twilight.twilight.domain.bulletin.post.controller;

import com.twilight.twilight.domain.bulletin.post.dto.*;
import com.twilight.twilight.domain.bulletin.post.service.FreeBoardPostService;
import com.twilight.twilight.global.search.NgramGenerator;
import com.twilight.twilight.global.search.NgramIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bulletin/free-board")
public class FreeBoardApiController {

    private final FreeBoardPostService freeBoardPostService;
    private final NgramGenerator ngramGenerator;

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

    @GetMapping("/search")
    public CursorResponse<GetFreeBoardPostListDto> getFreeBoardSearchPosts(
            PageCursorRequest pageRequest,
            @RequestParam String q
    ) {
        List<String> ngrams = ngramGenerator.generateByKeyword(q);
        int pageSize = pageRequest.pageSizeOrDefault();
        List<GetFreeBoardPostListDto> postLists = freeBoardPostService.getSearchPostsByCursor(pageRequest, q);

        return freeBoardPostService.getCursorResponse(postLists, pageSize);
    }

}
