package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class MemoryMemberRepository implements MemberRepository {
    private static final Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public void add(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
    }

    @Override
    public void update(Long id, Member updateMember) {
        Member findMember = store.get(id);

        findMember.setLoginId(updateMember.getLoginId());
        findMember.setPassword(updateMember.getPassword());
        findMember.setUsername(updateMember.getUsername());
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        List<Member> members = store.values().stream().toList();

        for (Member member : members) {
            if(member.getLoginId().equals(loginId)) {
                return Optional.ofNullable(member);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Member> findAll() {
        Collection<Member> memberList = store.values();
        return memberList.stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
