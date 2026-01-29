package com.twilight.twilight.domain.member.member.controller.view;

import com.twilight.twilight.domain.member.follow.Service.FollowService;
import com.twilight.twilight.domain.member.follow.dto.FollowCountResponse;
import com.twilight.twilight.domain.member.follow.dto.FollowStatusResponse;
import com.twilight.twilight.domain.member.member.dto.MyPageMemberInfoDto;
import com.twilight.twilight.domain.member.member.dto.MyPageUpdateDto;
import com.twilight.twilight.domain.member.member.dto.TargetMemberInfoResponse;
import com.twilight.twilight.domain.member.member.service.MemberService;
import com.twilight.twilight.global.authentication.springSecurity.domain.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberViewController {

    private final MemberService memberService;
    private final FollowService followService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/login";
    }
    
    //수정필요한 컨트롤러
    @GetMapping("/mypage")
    public String mypage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        String memberName = userDetails.getMember().getMemberName();
        model.addAttribute("memberName", memberName);

        FollowCountResponse followCount =
                followService.getFollowCount(userDetails.getMember().getMemberId());
        model.addAttribute("followCount", followCount);

        return "myPage/mypage";
    }

    @GetMapping("/mypage/detail")
    public String myPageDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
            ) {
        MyPageMemberInfoDto dto = memberService.getMyPageMemberInfo(userDetails.getMember());
        model.addAttribute("memberInfo", dto);
        return "myPage/mypage-detail";
    }

    @GetMapping("/mypage/edit")
    public String myPageEdit(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
            ) {
        MyPageMemberInfoDto dto = memberService.getMyPageMemberInfo(userDetails.getMember());
        model.addAttribute("memberInfo", dto);
        return "myPage/mypage-edit";
    }

    @PostMapping("/mypage/edit")
    public String updateMemberInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute MyPageUpdateDto formData
            ) {
        memberService.updateMemberInfo(userDetails.getMember(), formData);

        return "redirect:/mypage";
    }

    @GetMapping("/mypage/followers")
    public String myFollowers(Model model) {
        model.addAttribute("type", "followers");
        return "mypage/follow-list";
    }

    @GetMapping("/mypage/followings")
    public String myFollowings(Model model) {
        model.addAttribute("type", "followings");
        return "mypage/follow-list";
    }

    @GetMapping("/members/{targetMemberId}")
    public String getMemberInfo(
            @PathVariable Long targetMemberId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model
    ) {
        FollowStatusResponse followStatusResponse = followService.getFollowStatus(
                targetMemberId, customUserDetails.getMember().getMemberId()
        );
        model.addAttribute("followStatus", followStatusResponse);

        FollowCountResponse followCount =
                followService.getFollowCount(targetMemberId);
        model.addAttribute("followCount", followCount);

        TargetMemberInfoResponse memberInfo = memberService.getMemberInfo(targetMemberId);

        model.addAttribute("targetMemberId", targetMemberId);
        model.addAttribute("memberInfo", memberInfo);

        return "memberPage/member-page";
    }

}
