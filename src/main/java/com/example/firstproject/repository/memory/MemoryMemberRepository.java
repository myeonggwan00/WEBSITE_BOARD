package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.Member;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
@Slf4j
public class MemoryMemberRepository implements MemberRepository {
    private static final Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public void add(Member member) {
        member.setNo(++sequence);
        store.put(member.getNo(), member);
    }

    @Override
    public void update(Long no, Member updateMember) {
        Member findMember = store.get(no);

        findMember.setId(updateMember.getId());
        findMember.setPwd(updateMember.getPwd());
        findMember.setUserName(updateMember.getUserName());
    }

    @Override
    public Optional<Member> findByNo(Long no) {
        return Optional.ofNullable(store.get(no));
    }

    @Override
    public Optional<Member> findById(String id) {
        List<Member> members = store.values().stream().toList();

        for (Member member : members) {
            if(member.getId().equals(id)) {
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
    public void deleteByNo(Long no) {
        store.remove(no);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
