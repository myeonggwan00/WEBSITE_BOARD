package com.example.firstproject.repository.h2.member;

import com.example.firstproject.domain.Member;
import com.example.firstproject.jdbc.connection.DBConnectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class H2MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(id, password, username) values (?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnectionUtils.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, member.getId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getUsername());

            pstmt.executeUpdate();

            return member;
        } catch(SQLException e) {
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    public Member findByNo(Long id) throws SQLException{
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtils.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();

                member.setId(rs.getLong("member_id"));
                member.setLoginId(rs.getString("login_id"));
                member.setPassword(rs.getString("password"));
                member.setUsername(rs.getString("username"));

                return member;
            } else {
                throw new NoSuchElementException("member not found id = " + id);
            }
        } catch(SQLException e) {
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public Member findById(String id) throws SQLException{
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtils.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();

                member.setId(rs.getLong("member_id"));
                member.setLoginId(rs.getString("login_id"));
                member.setPassword(rs.getString("password"));
                member.setUsername(rs.getString("username"));

                return member;
            } else {
                throw new NoSuchElementException("member not found id = " + id);
            }
        } catch(SQLException e) {
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public void update(Member updateMember, Long id) throws SQLException {
        String sql = "update member set login_id = ?, password = ?, username = ? where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnectionUtils.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, updateMember.getLoginId());
            pstmt.setString(2, updateMember.getPassword());
            pstmt.setString(3, updateMember.getUsername());
            pstmt.setLong(4, id);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw e;
        } finally {
            close(conn, pstmt, null);
        }
    }

    public void close(Connection conn, Statement stmt, ResultSet rs) {
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
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
}
