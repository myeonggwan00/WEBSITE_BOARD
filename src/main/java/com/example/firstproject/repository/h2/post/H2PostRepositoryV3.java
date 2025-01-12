package com.example.firstproject.repository.h2.post;

import com.example.firstproject.domain.jdbc.Post;
import com.example.firstproject.domain.dto.SearchCondition;
import com.example.firstproject.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
        String sql = "insert into post (title, content, username, created_at, view_cnt) values (?, ?, ?, ?, ?)";

        template.update(sql, post.getTitle(), post.getContent(), post.getUsername(), Timestamp.valueOf(LocalDateTime.now()), post.getViewCnt());
    }

    @Override
    public void modify(Long id, Post updatePost) {
        String sql = "update post set title = ?, content = ?, created_at = ? where id = ?";

        template.update(sql, updatePost.getTitle(), updatePost.getContent(), Timestamp.valueOf(LocalDateTime.now()), id);
    }

    @Override
    public void updateViewCnt(Post post) {
        String sql = "update post set view_cnt = view_cnt + 1 where id = ?";

        template.update(sql, post.getId());
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "select * from post where id = ?";

        return Optional.ofNullable(template.queryForObject(sql, postRowMapper(), id));
    }

    @Override
    public List<Post> findAll(Integer offset, Integer limit) {
        String sql = "select * from post limit ? offset ?";

        return template.query(sql, postRowMapper(), limit, offset);
    }

    @Override
    public void remove(Long id) {
        String sql = "delete from post where id = ?";

        template.update(sql, id);
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

            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setUsername(rs.getString("username"));
            post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            post.setViewCnt(rs.getLong("view_cnt"));

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
