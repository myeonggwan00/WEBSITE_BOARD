package com.example.firstproject;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    void save(Post post);

    void modify(Long bno, Post updatePost);

    void remove(Long bno);

    Optional<Post> findByBno(Long bno);

    List<Post> findAll();
}
