package com.example.firstproject;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PostMemoryRepository implements PostRepository {
    private static final Map<Long, Post> postStore = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public void save(Post post) {
        post.setBno(++sequence);
        post.setRegisterTime(LocalDateTime.now());

        postStore.put(post.getBno(), post);
    }

    @Override
    public void modify(Long bno, Post updatePost) {
        Post findPost = postStore.get(bno);

        findPost.setTitle(updatePost.getTitle());
        findPost.setContent(updatePost.getContent());
        findPost.setRegisterTime(LocalDateTime.now());
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

    @Override
    public void remove(Long bno) {
        postStore.remove(bno);
    }

    @Override
    public List<Post> selectPage(Map<String, Integer> map) {
        Integer offset = map.get("offset");
        Integer pageSize = map.get("pageSize");

        List<Post> posts = postStore.values().stream().toList();
        List<Post> pagePostStore = new ArrayList<>();

        int offsetCheck = 1;
        int sizeCheck = 1;

        for (Post post : posts) {
            if(offsetCheck > offset) {
                if(sizeCheck <= pageSize) {
                    pagePostStore.add(post);
                }
                sizeCheck++;
            }
            offsetCheck++;
        }

        return pagePostStore;
    }

    @Override
    public int getCount() {
        return postStore.values().size();
    }
}
