package com.example.firstproject;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    /**
     * MemberRepository, PostRepository 의존성 관계 주입
     */
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    /**
     * 홈 화면 보여주는 메서드
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 로그아웃 요청을 처리하는 메서드
     *
     * 1. HTTP Session 가져오기
     * 2. 가져온 세션이 널인지 아닌지 확인
     * 3. 널이 아니면 세션의 invalidate 메서드를 사용하여 세션을 종료
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if(session != null) {
            // 세션 종료
            session.invalidate();

            log.info("로그아웃 성공");
        }

        return "redirect:/";
    }

    /**
     * 로그인 화면을 보여주는 메서드
     *
     * 나중에 로그인 정보를 주고 받기 위해서 비어있는 회원 객체를 저장소(Model)에 저장해서 폼으로 넘겨준다.
     */
    @GetMapping("/login")
    public String login(Model model, @CookieValue(name = "memberId", required = false) String memberId) {
        LoginMember loginMember;

        // 쿠키에 저장된 값을 조회해서 얻은 값이 null인 경우 -> 로그인 기억 기능을 사용X
        if(memberId == null) {
            loginMember = new LoginMember();
        }
        // 쿠키에 저장된 값을 조회해서 얻은 값이 null이 아닌 경우 -> 로그인 기억 기능을 사용
        else {
            loginMember = new LoginMember(memberId);
            loginMember.setRememberId(true);
        }

        model.addAttribute("loginMember", loginMember);

        return "loginForm";
    }

    /**
     * 로그인 처리를 하는 메서드
     *
     * 1. 로그인 화면에서 사용자가 입력한 정보를 가져오기 (@ModelAttribute 사용)
     * 2. 모든 회원 정보가 담겨있는 저장소에 사용자가 입력한 정보가 저장되어 있는지 확인하기
     * 3. 저장소에 저장이 되어있지 않은 회원인 경우 로그인 화면을 다시 보여주기
     * 4. 저장소에 저장이 되어있다면 로그인 처리(세션 생성, 홈 화면으로 이동)하기
     */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginMember loginMember, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        String redirectURL = request.getParameter("redirectURL");
        Optional<Member> optionalMember = memberRepository.findById(loginMember.getId());

        if(optionalMember.isEmpty() || !loginMember.getId().equals(optionalMember.get().getId()) || !loginMember.getPwd().equals(optionalMember.get().getPwd())) {
            log.info("로그인 실패");

            bindingResult.reject("loginFail");
        }

        if(bindingResult.hasErrors()) {
            loginMember.setId("");
            loginMember.setRememberId(false);

            return "loginForm";
        }

        // 로그인 기억 기능을 사용하는 경우
        if(loginMember.isRememberId()) {
            // 쿠키 생성
            Cookie cookie = new Cookie("memberId", loginMember.getId());

            response.addCookie(cookie);
        }
        // 로그인 기억 기능을 사용하지 않는 경우
        else {
            // 쿠키 삭제
            Cookie cookie = new Cookie("memberId", loginMember.getId());
            cookie.setMaxAge(0);

            response.addCookie(cookie);
        }

        // 세션 생성
        HttpSession session = request.getSession();

        // 세션에 정보 저장하기
        session.setAttribute(SessionConst.LOGIN_MEMBER, optionalMember.get());
        session.setAttribute("status", true); // 로그인 여부 확인하기 위한 작업

        log.info("로그인 성공");
        log.info("LOGIN loginMember={}", optionalMember.get());

        // 로그인 안한 상채에서 게시판 글 쓰기 버튼을 누른 경우
        if(redirectURL != null) {
            // 게시판 글 쓰기 화면으로 이동
            return "redirect:" + redirectURL;
        }

        return "redirect:/";
    }

    /**
     * 게시판 화면을 보여주는 메서드
     *
     * 회원이 작성한 게시글 정보를 처리하기 위해서 게시글이 저장되어 있는 저장소에서 모든 정보를 얻고 저장소(Model)에 담아서 화면으로 전송한다.
     */
    @GetMapping("/board")
    public String boardList(Model model) {
        List<Post> posts = postRepository.findAll();

        model.addAttribute("posts", posts);

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
        log.info("bindingResult={}", bindingResult);

        if(bindingResult.hasErrors()) {
            return "addBoard";
        }

        log.info("WRITE loginMember={}", loginMember);

        post.setUserName(loginMember.getUserName());

        postRepository.save(post);

        return "redirect:/board";
    }

    /**
     * 게시글을 선택했을 때 해당 게시글을 보여주는 메서드
     */
    @GetMapping("/board/{bno}")
    public String selectBoard(@PathVariable Long bno, Model model) {
        Post post = postRepository.findByBno(bno).get();

        post.increaseViewCnt();

        log.info("READ post={}", post);

        model.addAttribute("post", post);

        return "board";
    }

    /**
     * 게시글 수정 버튼을 눌렀을 때 게시글 수정 화면을 보여주는 메서드
     */
    @GetMapping("/board/{bno}/edit")
    public String modifyBoard(@PathVariable Long bno, Model model) {
        Post post = postRepository.findByBno(bno).get();


        log.info("MODIFY findPost={}", post);


        model.addAttribute("post", post);

        return "editBoard";
    }

    /**
     * 게시글 수정 및 등록 작업을 하는 메서드
     */
    @PostMapping("/board/{bno}/edit")
    public String modifyBoard(@PathVariable Long bno, @Validated @ModelAttribute Post updatePost, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "editBoard";
        }
        postRepository.modify(bno, updatePost);

        log.info("MODIFY updatePost={}", updatePost);

        return "redirect:/board";
    }


    /**
     * 게시글 삭제 작업을 하는 메서드
     */
    @PostMapping("/board/delete")
    public String deleteBoard(@ModelAttribute Post post) {
        postRepository.remove(post.getBno());

        return "redirect:/board";
    }

    /**
     * 회원가입 화면을 보여주는 메서드
     *
     * 사용자가 회원가입할 때 입력한 정보를 처리하기 위해서 비어있는 회원 객체를 저장소(Model)에 저장해서 폼에 넘겨준다.
     */
    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("member", new Member());

        return "registerForm";
    }

    /**
     * 회원가입 처리하는 메서드
     *
     * 1. 회원가입 화면에서 사용자가 입력한 정보를 얻기 (@ModelAttribute 사용)
     */
    @PostMapping("/register")
    public String register(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
        log.info("bindingResult={}", bindingResult);

        if(bindingResult.hasErrors()) {
            return "registerForm";
        }

        memberRepository.add(member);

        log.info("registerMember={}", member);

        return "home";
    }
}
