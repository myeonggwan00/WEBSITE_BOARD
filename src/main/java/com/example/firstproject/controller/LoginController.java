package com.example.firstproject.controller;

import com.example.firstproject.SessionConst;
import com.example.firstproject.domain.LoginMember;
import com.example.firstproject.domain.Member;
import com.example.firstproject.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    /**
     * 로그아웃 요청을 처리하는 메서드
     *
     * 1. HTTP Session 가져오기
     * 2. 가져온 세션이 널인지 아닌지 확인
     * 3. 널이 아니면 세션의 invalidate 메서드를 사용하여 세션을 종료
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if(session != null) {
            // 세션 종료
            session.invalidate();

            log.info("로그아웃 성공");
        }

        return "redirect:/";
    }

    /**
     * 로그인 화면을 보여주는 메서드
     *
     * 나중에 로그인 정보를 주고 받기 위해서 비어있는 회원 객체를 저장소(Model)에 저장해서 폼으로 넘겨준다.
     */
    @GetMapping("/login")
    public String login(Model model, @CookieValue(name = "memberId", required = false) String memberId) {
        LoginMember loginMember;

        // 쿠키에 저장된 값을 조회해서 얻은 값이 null인 경우 -> 로그인 기억 기능을 사용X
        if(memberId == null) {
            loginMember = new LoginMember();
        }
        // 쿠키에 저장된 값을 조회해서 얻은 값이 null이 아닌 경우 -> 로그인 기억 기능을 사용
        else {
            loginMember = new LoginMember(memberId);
            loginMember.setRememberId(true);
        }

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
        Optional<Member> optionalMember = memberRepository.findById(loginMember.getId());

        if(optionalMember.isEmpty() || !loginMember.getId().equals(optionalMember.get().getId()) || !loginMember.getPwd().equals(optionalMember.get().getPwd())) {
            log.info("로그인 실패");

            bindingResult.reject("loginFail");
        }

        if(bindingResult.hasErrors()) {
            loginMember.setId("");
            loginMember.setRememberId(false);

            return "loginForm";
        }

        // 로그인 기억 기능을 사용하는 경우
        if(loginMember.isRememberId()) {
            // 쿠키 생성
            Cookie cookie = new Cookie("memberId", loginMember.getId());

            response.addCookie(cookie);
        }
        // 로그인 기억 기능을 사용하지 않는 경우
        else {
            // 쿠키 삭제
            Cookie cookie = new Cookie("memberId", loginMember.getId());
            cookie.setMaxAge(0);

            response.addCookie(cookie);
        }

        // 세션 생성
        HttpSession session = request.getSession();

        // 세션에 정보 저장하기
        session.setAttribute(SessionConst.LOGIN_MEMBER, optionalMember.get());
        session.setAttribute("status", true); // 로그인 여부 확인하기 위한 작업

        log.info("로그인 성공");
        log.info("LOGIN loginMember={}", optionalMember.get());

        // 로그인 안한 상채에서 게시판 글 쓰기 버튼을 누른 경우
        if(redirectURL != null) {
            // 게시판 글 쓰기 화면으로 이동
            return "redirect:" + redirectURL;
        }

        return "redirect:" + request.getRequestURI();
    }

}
