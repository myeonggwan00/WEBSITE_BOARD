package com.example.firstproject.repository.h2.member;

import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.jdbc.connection.DBConnectionUtils;
import com.example.firstproject.repository.MemberRepository;
import com.example.firstproject.repository.ex.DbException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
public class H2MemberRepositoryV1 implements MemberRepository {
    @Override
    public void add(Member member) {
        String sql = "insert into member(login_id, password, username) values (?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, member.getLoginId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getUsername());

            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new DbException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void update(Long id, Member updateMember) {
        String sql = "update member set login_id = ?, password = ?, username = ? where id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, updateMember.getLoginId());
            pstmt.setString(2, updateMember.getPassword());
            pstmt.setString(3, updateMember.getUsername());
            pstmt.setLong(4, id);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new DbException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();

                member.setId(rs.getLong("id"));
                member.setLoginId(rs.getString("login_id"));
                member.setPassword(rs.getString("password"));
                member.setUsername(rs.getString("username"));

                return Optional.ofNullable(member);
            } else {
                throw new NoSuchElementException("member not found id = " + id);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from member where login_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, loginId);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                Member member = new Member();

                member.setId(rs.getLong("id"));
                member.setLoginId(rs.getString("login_id"));
                member.setPassword(rs.getString("password"));
                member.setUsername(rs.getString("username"));

                return Optional.ofNullable(member);
            } else {
                throw new NoSuchElementException("member not found loginId = " + loginId);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Member> memberList = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();


            if(rs != null) {
                while (rs.next()) {
                    Member member = new Member();

                    member.setId(rs.getLong("id"));
                    member.setLoginId(rs.getString("login_id"));
                    member.setPassword(rs.getString("password"));
                    member.setUsername(rs.getString("username"));

                    memberList.add(member);
                }

                return memberList;
            } else {
                throw new NoSuchElementException("memberList is not existed.");
            }
        } catch(SQLException e) {
            throw new DbException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from  member where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "delete from member";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e);
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
