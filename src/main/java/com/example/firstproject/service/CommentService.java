package com.example.firstproject.service;

import com.example.firstproject.domain.Comment;
import com.example.firstproject.domain.Member;
import com.example.firstproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment saveComment(Member loginMember, Long bno, Comment comment) {
        comment.setPostBno(bno);
        comment.setUserId(loginMember.getLoginId());

        return commentRepository.save(comment);
    }

    public void modifyComment(Long cno, String modifyComment) {
        commentRepository.modify(cno, modifyComment);
    }

    public List<Comment> getCommentsByPostBno(Long bno) {
        return commentRepository.findByPostBno(bno);
    }

    public List<Comment> getReplies() {
        return commentRepository.getReplies();
    }

    public void deleteCommentByCno(Long cno) {
        commentRepository.delete(cno);
    }

    public Comment saveReply(Member loginMember, Long bno, Long pcno, String replyContent) {
        Comment comment = new Comment();
        comment.setPcno(pcno);
        comment.setPostBno(bno);
        comment.setUserId(loginMember.getLoginId());
        comment.setContent(replyContent);

        return commentRepository.save(comment);
    }

}
