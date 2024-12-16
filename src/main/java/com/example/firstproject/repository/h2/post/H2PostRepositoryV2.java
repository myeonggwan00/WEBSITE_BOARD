package com.example.firstproject.repository.h2.post;

import com.example.firstproject.domain.Post;
import com.example.firstproject.domain.SearchCondition;
import com.example.firstproject.jdbc.connection.ConnectionConst;
import com.example.firstproject.jdbc.connection.DBConnectionUtils;
import com.example.firstproject.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
//@Repository
public class H2PostRepositoryV2 implements PostRepository {
    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public H2PostRepositoryV2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Override
    public void save(Post post) {
        String sql = "insert into post (title, content, userName, registerTime, viewCnt) values (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getUserName());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(5, post.getViewCnt());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("save", sql, e);
        } finally {
            close(con, pstmt, null);
        }

    }

    @Override
    public void modify(Long bno, Post updatePost) {
        String sql = "update post set title = ?, content = ?, registerTime = ? where bno = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, updatePost.getTitle());
            pstmt.setString(2, updatePost.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(4, bno);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("modify", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void updateViewCnt(Post post) {
        String sql = "update post set viewCnt = viewCnt + 1 where bno = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, post.getBno());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("updateViewCnt", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public Optional<Post> findByBno(Long bno) {
        String sql = "select * from post where bno = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, bno);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Post post = new Post();

                post.setBno(rs.getLong("bno"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setUserName(rs.getString("userName"));
                post.setRegisterTime(rs.getTimestamp("registerTime").toLocalDateTime());
                post.setViewCnt(rs.getLong("viewCnt"));

                return Optional.ofNullable(post);
            } else {
                throw new NoSuchElementException("member not found bno = " + bno);
            }
        } catch (SQLException e) {
            throw exTranslator.translate("findByBno", sql, e);
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

                    post.setBno(rs.getLong("bno"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUserName(rs.getString("userName"));
                    post.setRegisterTime(rs.getTimestamp("registerTime").toLocalDateTime());
                    post.setViewCnt(rs.getLong("viewCnt"));

                    postList.add(post);
                }
                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }

        } catch (SQLException e) {
            throw exTranslator.translate("findAll", sql, e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void remove(Long bno) {
        String sql = "delete from post where bno = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, bno);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("remove", sql, e);
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
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            rs = pstmt.executeQuery();

            if(rs != null) {
                while(rs.next()) {
                    Post post = new Post();

                    post.setBno(rs.getLong("bno"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUserName(rs.getString("userName"));
                    post.setRegisterTime(rs.getTimestamp("registerTime").toLocalDateTime());
                    post.setViewCnt(rs.getLong("viewCnt"));

                    postList.add(post);
                }

                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }
        } catch (SQLException e) {
            throw exTranslator.translate("findByTitle", sql, e);
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

                    post.setBno(rs.getLong("bno"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUserName(rs.getString("userName"));
                    post.setRegisterTime(rs.getTimestamp("registerTime").toLocalDateTime());
                    post.setViewCnt(rs.getLong("viewCnt"));

                    postList.add(post);
                }

                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }
        } catch (SQLException e) {
            throw exTranslator.translate("findByContent", sql, e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<Post> findByWriter(Integer offset, Integer limit, String keyword) {
        String sql = "select * from post where userName like ? limit ? offset ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Post> postList = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, "%"+keyword+"%");
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            rs = pstmt.executeQuery();

            if(rs != null) {
                while(rs.next()) {
                    Post post = new Post();

                    post.setBno(rs.getLong("bno"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setUserName(rs.getString("userName"));
                    post.setRegisterTime(rs.getTimestamp("registerTime").toLocalDateTime());
                    post.setViewCnt(rs.getLong("viewCnt"));

                    postList.add(post);
                }

                return postList;
            } else {
                throw new NoSuchElementException("postList is not existed.");
            }
        } catch (SQLException e) {
            throw exTranslator.translate("findByWriter", sql, e);
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
            throw exTranslator.translate("getCount", sql, e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw exTranslator.translate("getConnection", null, e);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
}
