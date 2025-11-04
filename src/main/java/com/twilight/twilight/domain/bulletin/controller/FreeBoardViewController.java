package com.twilight.twilight.domain.bulletin.controller;

import com.twilight.twilight.domain.bulletin.dto.*;
import com.twilight.twilight.domain.bulletin.service.FreeBoardPostService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/bulletin/free-board")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class FreeBoardViewController {

    private final FreeBoardPostService freeBoardPostService;

    @GetMapping("/list")
    private String freeBoardList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        List<GetFreeBoardPostListDto> list = freeBoardPostService.getFreeBoardPostsByStaticVariable();
        model.addAttribute("postList", list);
        Long numberOfPosts = freeBoardPostService.getTotalPostCount();
        model.addAttribute("postCount", numberOfPosts);
        return "bulletin/free-board-list";
    }

    @GetMapping("/{post-id}")
    private String getFreeBoardDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId,
            Model model
    ) {
        GetFreeBoardPostDetailDto postDetailDto = freeBoardPostService.getFreeBoardPostDetail(postId);
        model.addAttribute("postDetailDto", postDetailDto);
        List<GetFreeBoardPostReplyDto> dtoList = freeBoardPostService.getFreeBoardPostReplies(postId);
        model.addAttribute("replies", dtoList);

        return "bulletin/free-board-post-detail";
    }

    @GetMapping("/write")
    private String writeFreeBoardForm(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return "bulletin/bulletin-free-board-write";
    }

    @PostMapping()
    private String writeFreeBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute FreeBoardPostForm form
            ) {
        freeBoardPostService.savePost(userDetails.getMember(), form);
        return "redirect:/bulletin/free-board/list";
    }

    @GetMapping("/{post-id}/edit")
    private String editFreeBoardPostForm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId,
            Model model
    ) {
        GetFreeBoardPostEditDto dto =
                freeBoardPostService.getEditablePost(userDetails.getMember(), postId);
        model.addAttribute("postDetail", dto);
        return "bulletin/free-board-edit";
    }

    @PostMapping("/{post-id}")
    private String editFreeBoardPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId,
            @ModelAttribute FreeBoardPostForm form
    ) {
        freeBoardPostService.editPost(userDetails.getMember(), postId, form);
        return "redirect:/bulletin/free-board/list";
    }

    @DeleteMapping("/{post-id}")
    private String deleteFreeBoardPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId
    ) {
        freeBoardPostService.deletePost(userDetails.getMember(), postId);

        return "redirect:/bulletin/free-board/list";
    }


}
