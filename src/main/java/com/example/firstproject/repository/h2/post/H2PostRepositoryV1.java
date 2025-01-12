package com.example.firstproject.repository.h2.post;

import com.example.firstproject.domain.jdbc.Post;
import com.example.firstproject.domain.dto.SearchCondition;
import com.example.firstproject.jdbc.connection.DBConnectionUtils;
import com.example.firstproject.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
public class H2PostRepositoryV1 implements PostRepository {
    @Override
    public void save(Post post) {
        String sql = "insert into post (title, content, username, created_at, view_cnt) values (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getUsername());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(5, post.getViewCnt());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }

    }

    @Override
    public void modify(Long id, Post updatePost) {
        String sql = "update post set title = ?, content = ?, created_at = ? where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, updatePost.getTitle());
            pstmt.setString(2, updatePost.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(4, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void updateViewCnt(Post post) {
        String sql = "update post set view_cnt = view_cnt + 1 where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, post.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "select * from post where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Post post = new Post();

                post.setId(rs.getLong("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setUsername(rs.getString("username"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                post.setViewCnt(rs.getLong("view_cnt"));

                return Optional.ofNullable(post);
            } else {
                throw new NoSuchElementException("member not found id = " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<Post> findAll(Integer offset, Integer limit) {
        String sql = "select * from post limit ? offset ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Post> postList = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            rs = pstmt.executeQuery();

            if(rs != null) {
                while(rs.next()) {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUsername(rs.getString("username"));
                    post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    post.setViewCnt(rs.getLong("view_cnt"));

                    postList.add(post);
                }
                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void remove(Long id) {
        String sql = "delete from post where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }
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

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Post> postList = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, "%"+keyword+"%");
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);

            rs = pstmt.executeQuery();

            if(rs != null) {
                while(rs.next()) {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUsername(rs.getString("username"));
                    post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    post.setViewCnt(rs.getLong("view_cnt"));

                    postList.add(post);
                }

                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<Post> findByContent(Integer offset, Integer limit ,String keyword) {
        String sql = "select * from post where content like ? limit ? offset ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Post> postList = new ArrayList<>();


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);

            rs = pstmt.executeQuery();

            if(rs != null) {
                while(rs.next()) {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUsername(rs.getString("username"));
                    post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    post.setViewCnt(rs.getLong("view_cnt"));

                    postList.add(post);
                }

                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<Post> findByWriter(Integer offset, Integer limit, String keyword) {
        String sql = "select * from post where username like ? limit ? offset ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Post> postList = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, "%"+keyword+"%");
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);

            rs = pstmt.executeQuery();

            if(rs != null) {
                while(rs.next()) {
                    Post post = new Post();

                    post.setId(rs.getLong("id"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUsername(rs.getString("username"));
                    post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    post.setViewCnt(rs.getLong("view_cnt"));

                    postList.add(post);
                }

                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public int getCount() {
        String sql = "select count(*) from post";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                int count = rs.getInt(1);
                return count;
            } else {
                throw new NoSuchElementException("post not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    private Connection getConnection() {
        return DBConnectionUtils.getConnection();
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            } catch(SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
}
