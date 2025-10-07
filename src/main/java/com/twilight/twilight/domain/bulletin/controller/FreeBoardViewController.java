package com.twilight.twilight.domain.bulletin.controller;

import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostDto;
import com.twilight.twilight.domain.bulletin.service.FreeBoardPostService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        //List<GetFreeBoardPostDto> list = freeBoardPostService.getPostList();
       // model.addAttribute("post_list", list);
        return null;
    }




}
