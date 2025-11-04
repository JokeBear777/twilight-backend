package com.twilight.twilight.domain.bulletin.controller;

import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.entity.FreeBoardPost;
import com.twilight.twilight.domain.bulletin.service.FreeBoardPostService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import com.twilight.twilight.global.config.BulletinPageProps;
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
@RequestMapping("/bulletin")   // 클래스 레벨 경로
@RequiredArgsConstructor
@Slf4j
public class BulletinViewController {

    private final FreeBoardPostService freeBoardPostService;
    private final BulletinPageProps bulletinPageProps;

    @GetMapping()
    public String bulletin(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {
        List<GetFreeBoardPostListDto> freeBoardPostList = freeBoardPostService.getFreeBoardPosts(bulletinPageProps.getPostSize());
        model.addAttribute("freeBoardPostList", freeBoardPostList);
        //뷰에서 타임리프로 모델 데이터들 받자
        return "bulletin/bulletin";
    }

}
