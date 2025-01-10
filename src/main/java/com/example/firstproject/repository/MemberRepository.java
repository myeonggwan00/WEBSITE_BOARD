package com.example.firstproject.repository;


import com.example.firstproject.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void add(Member member);

    void update(Long id, Member updateMember);

    Optional<Member> findById(Long id);

    Optional<Member> findByLoginId(String loginId);

    List<Member> findAll();

    void deleteById(Long id);

    void deleteAll();
}
