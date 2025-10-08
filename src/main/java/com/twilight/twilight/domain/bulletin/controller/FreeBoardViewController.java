package com.twilight.twilight.domain.bulletin.controller;

import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostDetailDto;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostReplyDto;
import com.twilight.twilight.domain.bulletin.service.FreeBoardPostService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/bulletin/free-board")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class FreeBoardViewController {

    private final FreeBoardPostService freeBoardPostService;

    @GetMapping("/list")
    private String freeBoardList(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ,Model model) {
        List<GetFreeBoardPostListDto> list = freeBoardPostService.getFreeBoardPosts();
        model.addAttribute("post_list", list);
        Long numberOfPosts = freeBoardPostService.getTotalPostCount();
        model.addAttribute("post_count", numberOfPosts);
        return "bulletin-free-board-list";
    }

    @GetMapping("/{post-id}")
    private String getFreeBoardDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("post-id") Long postId
            ,Model model
    ) {
        /*

        GetFreeBoardPostDetailDto dto = freeBoardPostService.getFreeBoardPostDetail();
        model.addAttribute("post", dto);
        List<GetFreeBoardPostReplyDto> dtoList = freeBoardPostService.getFreeBoardPostReplies();
        model.addAttribute("replies, dtoList);
         */
        return null;
    }



}
