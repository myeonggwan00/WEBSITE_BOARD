package com.example.firstproject.repository.h2.post;

import com.example.firstproject.domain.Post;
import com.example.firstproject.domain.SearchCondition;
import com.example.firstproject.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
public class H2PostRepositoryV3 implements PostRepository {
    private final JdbcTemplate template;

    public H2PostRepositoryV3(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Post post) {
        String sql = "insert into post (title, content, userName, registerTime, viewCnt) values (?, ?, ?, ?, ?)";

        template.update(sql, post.getTitle(), post.getContent(), post.getUserName(), Timestamp.valueOf(LocalDateTime.now()), post.getViewCnt());
    }

    @Override
    public void modify(Long bno, Post updatePost) {
        String sql = "update post set title = ?, content = ?, registerTime = ? where bno = ?";

        template.update(sql, updatePost.getTitle(), updatePost.getContent(), Timestamp.valueOf(LocalDateTime.now()), bno);
    }

    @Override
    public void updateViewCnt(Post post) {
        String sql = "update post set viewCnt = viewCnt + 1 where bno = ?";

        template.update(sql, post.getBno());
    }

    @Override
    public Optional<Post> findByBno(Long bno) {
        String sql = "select * from post where bno = ?";

        return Optional.ofNullable(template.queryForObject(sql, postRowMapper(), bno));
    }

    @Override
    public List<Post> findAll(Integer offset, Integer limit) {
        String sql = "select * from post limit ? offset ?";

        return template.query(sql, postRowMapper(), limit, offset);
    }

    @Override
    public void remove(Long bno) {
        String sql = "delete from post where bno = ?";

        template.update(sql, bno);
    }

    @Override
    public List<Post> selectPage(Map<String, Integer> map, SearchCondition sc) {
        Integer offset = map.get("offset");
        Integer pageSize = map.get("pageSize");

        if(sc.getOption().equals("C")) {
            return findByContent(offset, pageSize, sc.getKeyword());
        }
        else if(sc.getOption().equals("T")) {
            return findByTitle(offset, pageSize, sc.getKeyword());
        }
        else if(sc.getOption().equals("W")) {
            return findByWriter(offset, pageSize, sc.getKeyword());
        }
        else {
            return findAll(offset, pageSize);
        }
    }

    @Override
    public List<Post> findByTitle(Integer offset, Integer limit, String keyword) {
        String sql = "select * from post where title like ? limit ? offset ?";

        return template.query(sql, postRowMapper() ,"%" + keyword + "%", limit, offset);
    }

    @Override
    public List<Post> findByContent(Integer offset, Integer limit ,String keyword) {
        String sql = "select * from post where content like ? limit ? offset ?";

        return template.query(sql, postRowMapper(), "%" + keyword + "%", limit, offset);
    }

    @Override
    public List<Post> findByWriter(Integer offset, Integer limit, String keyword) {
        String sql = "select * from post where userName like ? limit ? offset ?";

        return template.query(sql, postRowMapper() ,"%" + keyword + "%", limit, offset);
    }

    private RowMapper<Post> postRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();

            post.setBno(rs.getLong("bno"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setUserName(rs.getString("userName"));
            post.setRegisterTime(rs.getTimestamp("registerTime").toLocalDateTime());
            post.setViewCnt(rs.getLong("viewCnt"));

            return post;
        };
    }

    @Override
    public int getCount() {
        String sql = "select count(*) from post";

        return template.queryForObject(sql, (rs, rowNum) -> {
            return rs.getInt(1);
        });
    }
}
