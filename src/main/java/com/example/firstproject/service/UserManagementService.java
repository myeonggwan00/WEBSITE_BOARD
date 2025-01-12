package com.example.firstproject.service;

import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final MemberRepository memberRepository;

    /**
     * 회원을 추가하는 메서드, 즉 회원가입 기능을 하는 메서드
     */
    public void addMember(Member member) {
        memberRepository.add(member);
    }

    /**
     * 회원을 삭제하는 메서드, 즉 회원탈퇴 기능을 하는 메서드
     */
    public void deleteMember(Member member) {
        memberRepository.deleteById(member.getId());
    }

    /**
     * 회원탈퇴 시, 회원탈퇴 전에 로그인으로 인해서 생성된 세션을 종료시키는 메서드
     */
    public void terminateSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
