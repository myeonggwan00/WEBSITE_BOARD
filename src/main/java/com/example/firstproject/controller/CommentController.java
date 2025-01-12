package com.example.firstproject.controller;

import com.example.firstproject.SessionConst;
import com.example.firstproject.domain.jdbc.Comment;
import com.example.firstproject.domain.jdbc.Member;
import com.example.firstproject.domain.jdbc.Post;
import com.example.firstproject.service.BoardService;
import com.example.firstproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final BoardService boardService;
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comment")
    public String comment(@PathVariable Long postId, Comment comment, Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Post selectedPost = boardService.getPostInfo(postId);

        Comment savedComment = commentService.saveComment(loginMember, postId, comment);
        List<Comment> comments = commentService.getCommentsByPostId(postId);

        model.addAttribute("postBno", postId);
        model.addAttribute("post", selectedPost);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("savedComment", savedComment);
        model.addAttribute("comments", comments);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteCommentByCommentId(commentId);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comment/{commentId}/modify")
    public String modifyComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam String modifyContent,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        log.info("edit cno={}, comment={}", commentId, modifyContent);

        commentService.modifyComment(commentId, modifyContent);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/posts/{postId}/comment/{parentCommentId}/reply")
    public String addReply(@PathVariable Long postId, @PathVariable Long parentCommentId, @RequestParam String replyContent,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        log.info("loginMember={}", loginMember);
        log.info("reply pcno={}, comment={}", parentCommentId, replyContent);

        commentService.saveReply(loginMember, postId, parentCommentId, replyContent);

        return "redirect:/posts/" + postId;
    }
}
