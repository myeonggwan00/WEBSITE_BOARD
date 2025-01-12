package com.example.firstproject.controller;

import com.example.firstproject.domain.dto.LoginMember;
import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.repository.MemberRepository;
import com.example.firstproject.service.LoginManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {
    /**
     * MemberRepository 의존성 관계 주입
     */
    private final MemberRepository memberRepository;
    private final LoginManagementService loginManagementService;

    /**
     * 로그아웃 요청을 처리하는 메서드
     *
     * 1. HTTP Session 가져오기
     * 2. 가져온 세션이 널인지 아닌지 확인
     * 3. 널이 아니면 세션의 invalidate 메서드를 사용하여 세션을 종료
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        loginManagementService.processLogout(request);

        return "redirect:/";
    }

    /**
     * 로그인 화면을 보여주는 메서드
     *
     * 나중에 로그인 정보를 주고 받기 위해서 비어있는 회원 객체를 저장소(Model)에 저장해서 폼으로 넘겨준다.
     */
    @GetMapping("/login")
    public String login(Model model, @CookieValue(name = "memberId", required = false) String memberId) {
        LoginMember loginMember = loginManagementService.processRememberIdLoginMember(memberId);

        model.addAttribute("loginMember", loginMember);

        return "loginForm";
    }

    /**
     * 로그인 처리를 하는 메서드
     *
     * 1. 로그인 화면에서 사용자가 입력한 정보를 가져오기 (@ModelAttribute 사용)
     * 2. 모든 회원 정보가 담겨있는 저장소에 사용자가 입력한 정보가 저장되어 있는지 확인하기
     * 3. 저장소에 저장이 되어있지 않은 회원인 경우 로그인 화면을 다시 보여주기
     * 4. 저장소에 저장이 되어있다면 로그인 처리(세션 생성, 홈 화면으로 이동)하기
     */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginMember loginMember, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        String redirectURL = request.getParameter("redirectURL");

        Optional<Member> optionalFindMember = loginManagementService.findLoginMemberById(loginMember.getId());

        loginManagementService.processLoginFail(loginMember, bindingResult, optionalFindMember);

        if(bindingResult.hasErrors()) {
            loginMember.setId("");
            loginMember.setRememberId(false);

            return "loginForm";
        }

        loginManagementService.processRememberIdCookie(loginMember,response);

        loginManagementService.createSession(request, optionalFindMember);

        log.info("LOGIN SUCCESS loginMember={}", optionalFindMember.get());

        // 로그인 안한 상채에서 게시판 글 쓰기 버튼을 누른 경우
        if(redirectURL != null) {
            // 게시판 글 쓰기 화면으로 이동
            return "redirect:" + redirectURL;
        }

        return "redirect:/";
    }
}