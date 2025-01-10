package com.example.firstproject.repository.h2.member;

import com.example.firstproject.domain.Member;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * 스프링이 제공하는 데이터 접근 예외 추상화와 SQL 예외 변환기 적용
 */
@Repository
@Slf4j
public class H2MemberRepositoryV3 implements MemberRepository {
    private final JdbcTemplate template;

    public H2MemberRepositoryV3(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(Member member) {
        String sql = "insert into member(login_id, password, username) values (?, ?, ?)";

        template.update(sql, member.getLoginId(), member.getPassword(), member.getUsername());
    }

    @Override
    public void update(Long id, Member updateMember) {
        String sql = "update member set login_id = ?, password = ?, username = ? where id = ?";

        template.update(sql, updateMember.getLoginId(), updateMember.getPassword(), updateMember.getUsername(), id);
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        try {
            Member member = template.queryForObject(sql, memberRowMapper(), id);

            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from member where login_id = ?";
        try {
            Member member = template.queryForObject(sql, memberRowMapper(), loginId);

            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        return template.query(sql, memberRowMapper());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from member where id = ?";

        template.update(sql, id);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from member";

        template.update(sql);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();

            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("login_id"));
            member.setPassword(rs.getString("password"));
            member.setUsername(rs.getString("username"));

            return member;
        };
    }
}
