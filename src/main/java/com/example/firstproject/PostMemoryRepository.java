package com.example.firstproject;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class PostMemoryRepository implements PostRepository {
    private static Map<Long, Post> postStore = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public void save(Post post) {
        post.setBno(++sequence);
        post.setRegisterTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")));

        postStore.put(post.getBno(), post);
    }

    @Override
    public void modify(Long bno, Post updatePost) {
        Post findPost = postStore.get(bno);

        findPost.setTitle(updatePost.getTitle());
        findPost.setContent(updatePost.getContent());
        findPost.setRegisterTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")));

    }

    @Override
    public void remove(Long bno) {
        postStore.remove(bno);
    }

    @Override
    public Optional<Post> findByBno(Long bno) {
        return Optional.ofNullable(postStore.get(bno));
    }

    @Override
    public List<Post> findAll() {
        Collection<Post> postList = postStore.values();
        return postList.stream().toList();
    }
}
