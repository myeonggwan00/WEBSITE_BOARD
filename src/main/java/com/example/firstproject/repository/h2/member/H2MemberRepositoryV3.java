package com.example.firstproject.repository.h2.member;

import com.example.firstproject.domain.Member;
import com.example.firstproject.jdbc.connection.DBConnectionUtils;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
        String sql = "insert into member(id, pwd, userName) values (?, ?, ?)";

        template.update(sql, member.getId(), member.getPwd(), member.getUserName());
    }

    @Override
    public void update(Long no, Member updateMember) {
        String sql = "update member set id = ?, pwd = ?, userName = ? where no = ?";

        template.update(sql, updateMember.getId(), updateMember.getPwd(), updateMember.getUserName(), no);
    }

    @Override
    public Optional<Member> findByNo(Long no) {
        String sql = "select * from member where no = ?";

        try {
            Member member = template.queryForObject(sql, memberRowMapper(), no);

            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Member> findById(String id) {
        String sql = "select * from member where id = ?";
        try {
            Member member = template.queryForObject(sql, memberRowMapper(), id);

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
    public void deleteByNo(Long no) {
        String sql = "delete from  member where no = ?";

        template.update(sql, no);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from  member";

        template.update(sql);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();

            member.setNo(rs.getLong("no"));
            member.setId(rs.getString("id"));
            member.setPwd(rs.getString("pwd"));
            member.setUserName(rs.getString("userName"));

            return member;
        };
    }
}
