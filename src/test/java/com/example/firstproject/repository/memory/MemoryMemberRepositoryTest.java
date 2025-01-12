package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class
MemoryMemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        memberRepository.deleteAll();
    }

    @Test
    void add() {
        Member newMember = new Member("test", "test123", "홍길동");

        memberRepository.add(newMember);

        Member findMember = memberRepository.findById(newMember.getId()).get();

        assertThat(findMember).isEqualTo(newMember);
    }

    @Test
    void update() {
        Member newMember = new Member("test", "test123", "홍길동");

        memberRepository.add(newMember);

        Member updateMember = new Member("update", "update123", "길동이");

        memberRepository.update(newMember.getId(), updateMember);

        assertThat(newMember.getLoginId()).isEqualTo("update");
        assertThat(newMember.getPassword()).isEqualTo("update123");
        assertThat(newMember.getUsername()).isEqualTo("길동이");
    }

    @Test
    void findById() {
        Member newMember = new Member("test", "test123", "홍길동");

        memberRepository.add(newMember);


        Member findMember = memberRepository.findByLoginId(newMember.getLoginId()).get();

        log.info("findMember={}", findMember);

        assertThat(findMember).isEqualTo(newMember);
    }

    @Test
    void findAll() {
        Member newMember1 = new Member("test1", "test123", "홍");
        Member newMember2 = new Member("test2", "test123", "홍길");
        Member newMember3 = new Member("test3", "test123", "홍길동");

        memberRepository.add(newMember1);
        memberRepository.add(newMember2);
        memberRepository.add(newMember3);


        List<Member> result = memberRepository.findAll();

        for (Member member : result) {
            log.info("member={}", member);
        }

        log.info("count={}", result.size());

        assertThat(result.size()).isEqualTo(3);
        assertThat(result).containsExactly(newMember1, newMember2, newMember3);
    }

    @Test
    void removeById() {
        Member newMember1 = new Member("test1", "test123", "홍");
        Member newMember2 = new Member("test2", "test123", "홍길");
        Member newMember3 = new Member("test3", "test123", "홍길동");

        memberRepository.add(newMember1);
        memberRepository.add(newMember2);
        memberRepository.add(newMember3);

        memberRepository.deleteById(newMember1.getId());

        List<Member> result = memberRepository.findAll();

        for (Member member : result) {
            log.info("member={}", member);
        }

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void removeAll() {
        Member newMember1 = new Member("test1", "test123", "홍");
        Member newMember2 = new Member("test2", "test123", "홍길");
        Member newMember3 = new Member("test3", "test123", "홍길동");

        memberRepository.add(newMember1);
        memberRepository.add(newMember2);
        memberRepository.add(newMember3);

        memberRepository.deleteAll();

        List<Member> result = memberRepository.findAll();

        log.info("count={}",result.size());

        assertThat(result.size()).isEqualTo(0);

    }
}