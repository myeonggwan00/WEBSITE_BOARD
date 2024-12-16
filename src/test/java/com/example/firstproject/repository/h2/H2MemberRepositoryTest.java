package com.example.firstproject.repository.h2;

import com.example.firstproject.domain.Member;
import com.example.firstproject.repository.h2.member.H2MemberRepositoryV0;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class H2MemberRepositoryTest {
    H2MemberRepositoryV0 memberRepository = new H2MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        Member member = new Member("h2", "1234", "mg");
        memberRepository.save(member);
        Member addMember = memberRepository.findById(member.getId());
        log.info("addMember={}", addMember);

        Member findMember = memberRepository.findByNo(addMember.getNo());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(addMember);

        memberRepository.update(new Member("update", "updateTest", "tester"), findMember.getNo());
        Member updateMember = memberRepository.findByNo(findMember.getNo());
        log.info("updateMember={}", updateMember);
        assertThat(updateMember.getId()).isEqualTo("update");
    }
}