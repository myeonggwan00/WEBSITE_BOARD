package com.example.firstproject.repository;

import com.example.firstproject.domain.jdbc.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    void modify(Long cno, String modifyComment);

    void delete(Long cno);

    List<Comment> findByPostId(Long postId);

    List<Comment> getReplies();

    Comment findByCommentId(Long cno);
}
