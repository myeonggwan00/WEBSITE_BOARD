package com.example.firstproject.repository;

import com.example.firstproject.domain.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    void modify(Long cno, String modifyComment);

    void delete(Long cno);

    List<Comment> findByPostBno(Long postBno);

    List<Comment> getReplies();

    Comment findByCno(Long cno);
}
