package com.example.firstproject.repository;

import com.example.firstproject.domain.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PostRepository {
    void save(Post post);

    void modify(Long bno, Post updatePost);

    Optional<Post> findByBno(Long bno);

    List<Post> findAll();

    void remove(Long bno);

    List<Post> selectPage(Map<String, Integer> map, List<Post> findPost);

    List<Post> findByTitle(String keyword);

    List<Post> findByContent(String keyword);

    List<Post> findByWriter(String keyword);

    int getCount();
}
