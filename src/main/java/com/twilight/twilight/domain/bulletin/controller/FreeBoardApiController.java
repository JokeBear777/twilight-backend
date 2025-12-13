package com.twilight.twilight.domain.bulletin.controller;

import com.twilight.twilight.domain.bulletin.dto.Cursor;
import com.twilight.twilight.domain.bulletin.dto.CursorResponse;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.dto.PageCursorRequest;
import com.twilight.twilight.domain.bulletin.service.FreeBoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        Cursor cursor = pageRequest.toCursor();
        int pageSize = pageRequest.pageSizeOrDefault();
        Long lastId = Optional.ofNullable(cursor)
                .map(Cursor::lastId)
                .orElse(null);

        List<GetFreeBoardPostListDto> postLists = freeBoardPostService.getPostsByCursor(pageRequest);

        return freeBoardPostService.getCursorResponse(postLists, pageSize);
    }

}
