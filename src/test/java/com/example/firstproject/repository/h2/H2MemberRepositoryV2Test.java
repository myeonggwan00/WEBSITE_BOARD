package com.example.firstproject.repository.h2;

import com.example.firstproject.domain.Member;
import com.example.firstproject.repository.h2.member.H2MemberRepositoryV2;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.example.firstproject.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class H2MemberRepositoryV2Test {
    H2MemberRepositoryV2 memberRepository;

    @BeforeEach
    void beforeEach() {
        // DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        memberRepository = new H2MemberRepositoryV2(dataSource);
    }

    @Test
    void crud() {
        // 회원 등록
        Member memberA = new Member("h2", "1234", "memberA");
        Member memberB = new Member("h2", "1234", "memberB");

        memberRepository.add(memberA);
        memberRepository.add(memberB);

        // 회원 아이디로 회원 조회(memberA 조회)
        Optional<Member> addMember = memberRepository.findByLoginId(memberA.getLoginId());

        // 회원 번호로 회원 조회(memberA 조회)
        Optional<Member> findMember = memberRepository.findById(addMember.get().getId());

        assertThat(findMember).isEqualTo(addMember);

        // 회원 정보 변경
        memberRepository.update(findMember.get().getId(), new Member("updateId", "updatePwd", "memberA"));

        Optional<Member> updateMember = memberRepository.findById(findMember.get().getId());

        assertThat(updateMember.get().getLoginId()).isEqualTo("updateId");

        List<Member> memberList = memberRepository.findAll();

        assertThat(memberList.size()).isEqualTo(2);

        memberRepository.deleteById(addMember.get().getId());

        memberList = memberRepository.findAll();

        assertThat(memberList.size()).isEqualTo(1);

        memberRepository.deleteAll();

        memberList = memberRepository.findAll();

        assertThat(memberList.size()).isEqualTo(0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}