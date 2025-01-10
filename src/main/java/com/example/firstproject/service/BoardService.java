package com.example.firstproject.service;

import com.example.firstproject.domain.Member;
import com.example.firstproject.domain.PageHandler;
import com.example.firstproject.domain.Post;
import com.example.firstproject.domain.SearchCondition;
import com.example.firstproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final PostRepository postRepository;

    /**
     * 검색 조건에 따라서 얻을 수 있는 게시글 리스트를 반환하는 메서드
     */
//    public List<Post> getPostsBySearchOption(SearchCondition searchCondition) {
//        String option = searchCondition.getOption();
//        String keyword = searchCondition.getKeyword();
//
//        if(option.equals("C")) {
//            return postRepository.findByContent(keyword);
//        }
//        else if(option.equals("T")) {
//            return postRepository.findByTitle(keyword);
//        }
//        else if(option.equals("W")) {
//            return postRepository.findByWriter(keyword);
//        }
//        else
//            return postRepository.findAll();
//    }

    /**
     * 게시판 페이징 처리를 위해서 PageHandler를 생성해서 반환하는 메서드
     */
    public PageHandler getPageHandler(Integer page, Integer pageSize) {
        return new PageHandler(postRepository.getCount(), page, pageSize);
    }

    /**
     * 게시판 페이징 처리하는데 필요한 페이지 정보를 반환하는 메서드
     * - 페이지 정보란 예를 들어서 MySQL의 OFFSET, LIMIT에 대한 정보를 의미
     */
    public Map<String, Integer> getPageInfo(Integer page, Integer pageSize) {
        Map<String, Integer> map = new HashMap<>();

        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);

        return map;
    }

    /**
     * 검색 조건으로 얻은 게시글 목록과 페이지 정보를 이용해서 페이징 처리된, 즉 해당 페이지에 존재하는 게시글 리스트를 반환하는 메서드
     */
//    public List<Post> getPostsByPage(Map<String, Integer> pageInfo, List<Post> findPost) {
    public List<Post> getPostsByPage(Map<String, Integer> pageInfo, SearchCondition sc) {
        return postRepository.selectPage(pageInfo, sc);
    }

    /**
     * 게시글을 저장하는 메서드
     */
    public void savePost(Member loginMember, Post post) {
        post.setUserName(loginMember.getUsername());
        postRepository.save(post);
    }

    /**
     * 게시글에 대한 정보를 얻어오는 메서드
     */
    public Post getPostInfo(Long bno) {
        return postRepository.findByBno(bno).get();
    }

    /**
     * 게시글의 조회수를 증가시키는 메서드
     */
    public void increaseViewCnt(Post post) {
        log.info("post={}", post);
        post.increaseViewCnt();
        log.info("cnt={}", post.getViewCnt());
        postRepository.updateViewCnt(post);
    }

    /**
     * 게시글을 수정하는 메서드
     */
    public void modifyPost(Long bno, Post updatedPost) {
        postRepository.modify(bno, updatedPost);
    }

    /**
     * 게시글을 삭제하는 메서드
     */
    public void removePost(Post post) {
        postRepository.remove(post.getBno());
    }

    /**
     * 페이지를 검사하는 메서드
     *
     * 예를 들어서 16페이지에 게시글이 하나 존재한다고 가정해보면?
     * 해당 게시글을 삭제하면 15페이지로 가야지 사용자가 더 편리하고 프로그램상 맞다.
     * 즉, 해당 게시글을 삭제하면 16페이지에는 아무 게시글이 없는데 해당 페이지를 보여줄 필요가 없다는 말이다.
     * 이러한 점을 방지하기 위해 해당 메서드를 도입했다.
     */
    public Integer checkPage(Integer page) {
        if(postRepository.getCount() < page * 10 - 9)
            return page - 1;

        return page;
    }

    /**
     * 페이지를 구하는 메서드
     *
     * 게시글 작성할 때 마다 맨 첫 번쩨 페이지로 가는 것이 불편해서 해당 메서드 도입
     * 해당 메서드를 도입함으로써 게시글을 작성했을 때 작성한 게시글이 존재하는 게시판 페이지로 이동이 가능해졌다.
     * 이전에는 게시글 작성시 무조건 첫 번째 게시판 페이지로 이동하였다.
     */
    public Integer findPage() {
        int totalCnt = postRepository.getCount();

        if(totalCnt != 0) {
            if(totalCnt % 10 == 0) {
                return totalCnt / 10;
            } else {
                return totalCnt / 10 + 1;
            }
        } else {
            return 1;
        }
    }
}
