package com.example.firstproject.controller;

import com.example.firstproject.SessionConst;
import com.example.firstproject.domain.Comment;
import com.example.firstproject.domain.Member;
import com.example.firstproject.domain.Post;
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

    @PostMapping("/board/{bno}/comment")
    public String comment(@PathVariable Long bno, Comment comment, Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Post selectedPost = boardService.getPostInfo(bno);

        Comment savedComment = commentService.saveComment(loginMember, bno, comment);
        List<Comment> comments = commentService.getCommentsByPostBno(bno);

        model.addAttribute("postBno", bno);
        model.addAttribute("post", selectedPost);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("savedComment", savedComment);
        model.addAttribute("comments", comments);

        return "redirect:/board/" + bno;
    }

    @PostMapping("/board/{bno}/comment/{cno}/delete")
    public String deleteComment(@PathVariable Long bno, @PathVariable Long cno) {
        commentService.deleteCommentByCno(cno);

        return "redirect:/board/" + bno;
    }

    @PostMapping("/board/{bno}/comment/{cno}/modify")
    public String modifyComment(@PathVariable Long bno, @PathVariable Long cno, @RequestParam String modifyContent, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        log.info("edit cno={}, comment={}", cno, modifyContent);

        commentService.modifyComment(cno, modifyContent);

        return "redirect:/board/" + bno;
    }

    @PostMapping("/board/{bno}/comment/{pcno}/reply")
    public String addReply(@PathVariable Long bno, @PathVariable Long pcno, @RequestParam String replyContent, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        log.info("loginMember={}", loginMember);
        log.info("reply pcno={}, comment={}", pcno, replyContent);

        commentService.saveReply(loginMember, bno, pcno, replyContent);

        return "redirect:/board/" + bno;
    }
}
