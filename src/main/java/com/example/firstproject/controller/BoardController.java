package com.example.firstproject.controller;

import com.example.firstproject.SessionConst;
import com.example.firstproject.domain.*;
import com.example.firstproject.service.BoardService;
import com.example.firstproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @ModelAttribute("searchOptions")
    public List<SearchOption> searchOptions() {
        List<SearchOption> searchOptions = new ArrayList<>();

        searchOptions.add(new SearchOption("C", "내용"));
        searchOptions.add(new SearchOption("T", "제목"));
        searchOptions.add(new SearchOption("W", "작성자"));

        return searchOptions;
    }

    /**
     * 게시판 화면을 보여주는 메서드
     *
     * 회원이 작성한 게시글 정보를 처리하기 위해서 게시글이 저장되어 있는 저장소에서 모든 정보를 얻고 저장소(Model)에 담아서 화면으로 전송한다.
     */
    @GetMapping("/board")
    public String boardList(Model model, @RequestParam(defaultValue = "1") Integer page,  @RequestParam(defaultValue = "10") Integer pageSize, SearchCondition sc) {
        Map<String, Integer> map = boardService.getPageInfo(page, pageSize);

        List<Post> posts = boardService.getPostsByPage(map, sc);
        PageHandler pageHandler = boardService.getPageHandler(page, pageSize);

        model.addAttribute("posts", posts);
        model.addAttribute("pageHandler", pageHandler);
        model.addAttribute("searchCondition", sc);

        return "boardList";
    }

    /**
     * 게시글 작성 화면 보여주는 메서드
     *
     * 회원이 작성한 게시글 정보를 처리하기 위해서 비어있는 게시글 객체를 저장소(Model)에 저장해서 폼으로 넘겨준다.
     */
    @GetMapping("/board/write")
    public String board(Model model) {
        model.addAttribute("post", new Post());

        return "addBoard";
    }

    /**
     * 회원이 작성한 게시글을 처리하는 메서드
     *
     * 1. 회원이 작성한 게시글 정보 얻기 (@ModelAttribute 사용)
     * 2. 로그인되어 있는 회원 정보를 알아내기 위해서 세션에 저장되어 있는 정보를 얻어오기 (@SessionAttribute 사용)
     * 3. 로그인되어 있는 회원으로 게시판에 게시글 저장하기
     */
    @PostMapping("/board/write")
    public String board(@Validated @ModelAttribute Post post, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        if(bindingResult.hasErrors())
            return "addBoard";

        log.info("WRITE loginMember={}", loginMember);
        log.info("post={}", post);

        boardService.savePost(loginMember, post);

        return "redirect:/board?page=" + boardService.findPage() + "&pageSize=10";
    }

    /**
     * 게시글을 선택했을 때 해당 게시글을 보여주는 메서드
     */
    @GetMapping("/board/{bno}")
    public String selectPost(@PathVariable Long bno, Model model, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        Comment comment = new Comment();

        Post selectedPost = boardService.getPostInfo(bno);

        boardService.increaseViewCnt(selectedPost);

        List<Comment> comments = commentService.getCommentsByPostBno(bno);
        List<Comment> replies = commentService.getReplies();

        model.addAttribute("post", selectedPost);
        model.addAttribute("comments", comments);
        model.addAttribute("replies", replies);
        model.addAttribute("comment", comment);
        model.addAttribute("loginMember", loginMember);

        return "board";
    }

    /**
     * 게시글 수정 버튼을 눌렀을 때 게시글 수정 화면을 보여주는 메서드
     */
    @GetMapping("/board/{bno}/edit")
    public String modifyBoard(@PathVariable Long bno, Model model) {
        Post post = boardService.getPostInfo(bno);

        log.info("MODIFY findPost={}", post);

        model.addAttribute("post", post);

        return "editBoard";
    }

    /**
     * 게시글 수정 및 등록 작업을 하는 메서드
     */
    @PostMapping("/board/{bno}/edit")
    public String modifyBoard(@PathVariable Long bno, Integer page, Integer pageSize, @Validated @ModelAttribute Post updatePost, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "editBoard";

        boardService.modifyPost(bno, updatePost);

        log.info("MODIFY updatePost={}", updatePost);

        return "redirect:/board?page=" + page + "&pageSize=" + pageSize;
    }

    /**
     * 게시글 삭제 작업을 하는 메서드
     */
    @PostMapping("/board/delete")
    public String deleteBoard(@ModelAttribute Post post, Integer page, Integer pageSize) {
        boardService.removePost(post);

        return "redirect:/board?page=" + boardService.checkPage(page) + "&pageSize=" + pageSize;
    }
}
