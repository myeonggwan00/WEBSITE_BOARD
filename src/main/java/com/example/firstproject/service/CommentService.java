package com.example.firstproject.service;

import com.example.firstproject.domain.jdbc.Comment;
import com.example.firstproject.domain.jdbc.Member;
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

    public Comment saveComment(Member loginMember, Long postId, Comment comment) {
        comment.setPostId(postId);
        comment.setUserId(loginMember.getLoginId());

        return commentRepository.save(comment);
    }

    public void modifyComment(Long commentId, String modifyComment) {
        commentRepository.modify(commentId, modifyComment);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getReplies() {
        return commentRepository.getReplies();
    }

    public void deleteCommentByCommentId(Long commentId) {
        commentRepository.delete(commentId);
    }

    public Comment saveReply(Member loginMember, Long postId, Long parentCommentId, String replyContent) {
        Comment comment = new Comment();
        comment.setParentCommentId(parentCommentId);
        comment.setPostId(postId);
        comment.setUserId(loginMember.getLoginId());
        comment.setContent(replyContent);

        return commentRepository.save(comment);
    }

}
