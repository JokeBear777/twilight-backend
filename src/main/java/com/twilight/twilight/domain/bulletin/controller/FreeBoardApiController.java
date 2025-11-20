package com.twilight.twilight.domain.bulletin.controller;

import com.twilight.twilight.domain.bulletin.service.FreeBoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bulletin/free-board-post")
public class FreeBoardApiController {

    private final FreeBoardPostService freeBoardPostService;

    /*
    @GetMapping("{post-id}")
    private ResponseEntity<List<GetFreeBoardPostReplyDto>>  getFreeBoardPost(
            @PathVariable(name = "post-id") Long postId) {

        return ResponseEntity.ok(freeBoardPostService.getFreeBoardPostReplies(postId));
    }
            */


}
