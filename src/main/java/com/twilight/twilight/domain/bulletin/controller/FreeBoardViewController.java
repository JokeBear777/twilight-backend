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
    public String freeBoardList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        List<GetFreeBoardPostListDto> list = freeBoardPostService.getFreeBoardPostsByStaticVariable();
        model.addAttribute("postList", list);
        Long numberOfPosts = freeBoardPostService.getTotalPostCount();
        model.addAttribute("postCount", numberOfPosts);
        return "bulletin/free-board-list";
    }

    @GetMapping("/{post-id}")
    public String getFreeBoardDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId,
            Model model
    ) {
        GetFreeBoardPostDetailDto postDetailDto = freeBoardPostService.getFreeBoardPostDetail(postId);
        model.addAttribute("post", postDetailDto);
        List<GetFreeBoardPostReplyDto> dtoList = freeBoardPostService.getFreeBoardPostReplies(postId);
        model.addAttribute("replies", dtoList);
        model.addAttribute("memberId", userDetails.getMember().getMemberId());

        return "bulletin/free-board-post-detail";
    }


    @PostMapping("/{post-id}/recommend")
    public String increaseRecommendation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId
    ) {
        freeBoardPostService.increasePostRecommendation(userDetails.getMember(), postId);

        return "redirect:/bulletin/free-board/{post-id}";
    }

    @GetMapping("/write")
    public String writeFreeBoardForm(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return "bulletin/bulletin-free-board-write";
    }

    @PostMapping("/write")
    public String writeFreeBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute FreeBoardPostForm form
            ) {
        freeBoardPostService.savePost(userDetails.getMember(), form);
        return "redirect:/bulletin/free-board/list";
    }

    @GetMapping("/{post-id}/edit")
    public String editFreeBoardPostForm(
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
    public String editFreeBoardPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId,
            @ModelAttribute FreeBoardPostForm form
    ) {
        freeBoardPostService.editPost(userDetails.getMember(), postId, form);
        return "redirect:/bulletin/free-board/list";
    }

    @PostMapping("/{post-id}/delete")
    public String deleteFreeBoardPost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId
    ) {
        freeBoardPostService.deletePost(userDetails.getMember(), postId);

        return "redirect:/bulletin/free-board/list";
    }

    /*
    *
    * ************ 리플 부분 *************
    *
    *  */

    @PostMapping("/{post-id}/reply")
    public String addComment(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId,
            Model model,
            @ModelAttribute FreeBoardPostReplyForm form
    ) {
        freeBoardPostService.postFreeBoardReply(postId, userDetails.getMember(), form);

        GetFreeBoardPostDetailDto postDetailDto = freeBoardPostService.getFreeBoardPostDetail(postId);
        List<GetFreeBoardPostReplyDto> dtoList = freeBoardPostService.getFreeBoardPostReplies(postId);

        model.addAttribute("post", postDetailDto);
        model.addAttribute("replies", dtoList);
        model.addAttribute("memberId", userDetails.getMember().getMemberId());

        //return "bulletin/free-board-post-detail :: replies-wrap";
        return "bulletin/free-board-post-detail :: replies";
    }

    @PostMapping("/{post-id}/reply/{reply-id}/delete")
    public String deleteReply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId,
            @PathVariable("reply-id") Long replyId,
            Model model
    ) {
        freeBoardPostService.deleteReply(userDetails.getMember(), replyId, postId);

        GetFreeBoardPostDetailDto postDetailDto = freeBoardPostService.getFreeBoardPostDetail(postId);
        List<GetFreeBoardPostReplyDto> dtoList = freeBoardPostService.getFreeBoardPostReplies(postId);

        model.addAttribute("post", postDetailDto);
        model.addAttribute("replies", dtoList);
        model.addAttribute("memberId", userDetails.getMember().getMemberId());

        return "bulletin/free-board-post-detail :: replies";
    }
    //삭제후 뷰 문제 해결해야함
}
