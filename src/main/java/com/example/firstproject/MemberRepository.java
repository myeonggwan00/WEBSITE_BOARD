package com.example.firstproject;


import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void add(Member member);

    void update(Long no, Member updateMember);

    Optional<Member> findByNo(Long no);

    Optional<Member> findById(String id);

    List<Member> findAll();

    void removeByNo(Long no);

    void removeAll();
}
