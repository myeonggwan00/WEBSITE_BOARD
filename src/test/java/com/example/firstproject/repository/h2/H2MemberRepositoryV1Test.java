package com.example.firstproject.repository.h2;

import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.repository.h2.member.H2MemberRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class H2MemberRepositoryV1Test {
    H2MemberRepositoryV1 memberRepository = new H2MemberRepositoryV1();

    @Test
    @Transactional
    void crud() {
        // 회원 등록
        Member memberA = new Member("h2", "1234", "memberA");
        Member memberB = new Member("h2", "1234", "memberB");

        memberRepository.add(memberA);
        memberRepository.add(memberB);

        // 회원 아이디로 회원 조회(memberA 조회)
        Optional<Member> addMember = memberRepository.findByLoginId(memberA.getLoginId());
        log.info("addMember={}", addMember);

        // 회원 번호로 회원 조회(memberA 조회)
        Optional<Member> findMember = memberRepository.findById(addMember.get().getId());
        log.info("findMember={}", findMember);

        assertThat(findMember).isEqualTo(addMember);

        // 회원 정보 변경
        memberRepository.update(findMember.get().getId(), new Member("updateId", "updatePwd", "memberA"));

        Optional<Member> updateMember = memberRepository.findById(findMember.get().getId());

        log.info("updateMember={}", updateMember);

        assertThat(updateMember.get().getLoginId()).isEqualTo("updateId");

        List<Member> memberList = memberRepository.findAll();

        log.info("memberList");

        for(Member member0 : memberList) {
            log.info("member={}", member0);
        }

        assertThat(memberList.size()).isEqualTo(2);

        memberRepository.deleteById(addMember.get().getId());

        memberList = memberRepository.findAll();

        assertThat(memberList.size()).isEqualTo(1);

        memberRepository.deleteAll();

        memberList = memberRepository.findAll();

        assertThat(memberList.size()).isEqualTo(0);
    }
}