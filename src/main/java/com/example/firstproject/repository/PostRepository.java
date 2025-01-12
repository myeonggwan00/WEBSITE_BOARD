package com.example.firstproject.repository;

import com.example.firstproject.domain.jdbc.Post;
import com.example.firstproject.domain.dto.SearchCondition;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostRepository {
    void save(Post post);

    void modify(Long bno, Post updatePost);

    void updateViewCnt(Post post);

    Optional<Post> findById(Long id);

    List<Post> findAll(Integer offset, Integer limit);

    void remove(Long bno);

//    List<Post> selectPage(Map<String, Integer> map, List<Post> findPost);
    List<Post> selectPage(Map<String, Integer> map, SearchCondition sc);

    List<Post> findByTitle(Integer offset, Integer limit, String keyword);

    List<Post> findByContent(Integer offset, Integer limit, String keyword);

    List<Post> findByWriter(Integer offset, Integer limit, String keyword);

    int getCount();
}
