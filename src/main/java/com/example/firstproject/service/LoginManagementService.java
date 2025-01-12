package com.example.firstproject.service;

import com.example.firstproject.SessionConst;
import com.example.firstproject.domain.dto.LoginMember;
import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginManagementService {
    private final MemberRepository memberRepository;

    public void processLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        log.info("session={}", session.getAttribute(SessionConst.LOGIN_MEMBER));

        session.invalidate();

        log.info("LOGOUT SUCCESS");
    }


    public LoginMember processRememberIdLoginMember(String memberId) {
        if (memberId == null) {
            return new LoginMember();
        } else {
            LoginMember loginMember = new LoginMember(memberId);
            loginMember.setRememberId(true);
            return loginMember;
        }
    }

    public void processLoginFail(LoginMember loginMember, BindingResult bindingResult, Optional<Member> optionalFindMember) {
        if(optionalFindMember.isEmpty() || !loginMember.getPwd().equals(optionalFindMember.get().getPassword())) {
            log.info("LOGIN FAIL");

            bindingResult.reject("loginFail");
        }
    }


    public Optional<Member> findLoginMemberById(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public void createSession(HttpServletRequest request, Optional<Member> optionalMember) {
        // 세션 생성
        HttpSession session = request.getSession();

        // 세션에 정보 저장하기
        session.setAttribute(SessionConst.LOGIN_MEMBER, optionalMember.get());
        session.setAttribute("status", true); // 로그인 여부 확인하기 위한 작업

        log.info("create session={}", session.getAttribute(SessionConst.LOGIN_MEMBER));
    }

    public void processRememberIdCookie(LoginMember loginMember, HttpServletResponse response) {
        if(loginMember.isRememberId()) {
            addCookie(response, loginMember);
        } else {
            deleteCookie(response, loginMember);
        }
    }

    private void addCookie(HttpServletResponse response, LoginMember loginMember) {
        // 쿠키 생성
        Cookie cookie = new Cookie("memberId", loginMember.getId());

        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse response, LoginMember loginMember) {
        // 쿠키 생성
        Cookie cookie = new Cookie("memberId", loginMember.getId());

        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

}
