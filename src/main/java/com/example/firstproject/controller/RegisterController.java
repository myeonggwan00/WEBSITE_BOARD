package com.example.firstproject.controller;

import com.example.firstproject.SessionConst;
import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.service.UserManagementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserManagementService userManagementService;

    /**
     * 회원가입 화면을 보여주는 메서드
     *
     * 사용자가 회원가입할 때 입력한 정보를 처리하기 위해서 비어있는 회원 객체를 저장소(Model)에 저장해서 폼에 넘겨준다.
     */
    @GetMapping("/members/new")
    public String register(Model model) {

        model.addAttribute("member", new Member());

        return "registerForm";
    }

    /**
     * 회원가입 처리하는 메서드
     *
     * 1. 회원가입 화면에서 사용자가 입력한 정보를 얻기 (@ModelAttribute 사용)
     */
    @PostMapping("/members/register")
    public String register(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "registerForm";

        userManagementService.addMember(member);

        log.info("registerMember={}", member);

        return "home";
    }

    @GetMapping("/members/delete")
    public String deleteAccount(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, HttpServletRequest request) {
        userManagementService.deleteMember(member);
        userManagementService.terminateSession(request);

        log.info("회원탈퇴 성공");

        return "redirect:/";
    }
}
