package com.example.firstproject.interceptor;

import com.example.firstproject.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession(false);

        /**
         * 로그인 여부를 확인
         * 만약 로그인이 되어있지 않으면 게시글을 작성하면 안되므로 로그인 창을 보여주도록 설정
         * 반대로 로그인이 되어있으면 게시글을 작성할 수 있으므로 게시글 작성 화면을 보여주도록 설정
         */
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");

            // 로그인 화면으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);

            return false;
        }

        return true;
    }
}
