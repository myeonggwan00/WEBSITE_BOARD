package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.jdbc.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;


/**
 * 게시글 저장소를 구현할 때 확장성을 고려하여 리포지토리 인터페이스를 설계하고, 이를 구현한 Java 기반 저장소를 만들었다.
 * 이후 데이터베이스(H2, MySQL 등)를 도입하면서 페이징과 같은 DB 특화 기능이 Java 코드와 달라서 이를 반영하기 위해 인터페이스 설계를 수정했다.
 * 결과적으로, Java 코드로 작성된 저장소는 인터페이스를 구현하지 않는 별도의 클래스 형태로 유지하고, 데이터베이스와 관련된 방식에 맞게 인터페이스를 수정했다.
 */
@Repository
public class PostMemoryRepository {
    private static final Map<Long, Post> postStore = new HashMap<>();
    private static Long sequence = 0L;

    public void save(Post post) {
        post.setId(++sequence);
        post.setCreatedAt(LocalDateTime.now());

        postStore.put(post.getId(), post);
    }

    public void modify(Long bno, Post updatePost) {
        Post findPost = postStore.get(bno);

        findPost.setTitle(updatePost.getTitle());
        findPost.setContent(updatePost.getContent());
        findPost.setCreatedAt(LocalDateTime.now());
    }

    public void updateViewCnt(Post post) {
        Post findPost = postStore.get(post.getId());

        findPost.setViewCnt(post.getViewCnt());
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(postStore.get(id));
    }

    public List<Post> findAll() {
        Collection<Post> postList = postStore.values();
        return postList.stream().toList();
    }

    public void remove(Long bno) {
        postStore.remove(bno);
    }

    public List<Post> selectPage(Map<String, Integer> map, List<Post> findPost) {
        Integer offset = map.get("offset");
        Integer pageSize = map.get("pageSize");

        List<Post> pagePostStore = new ArrayList<>();

        int offsetCheck = 1;
        int sizeCheck = 1;

        for (Post post : findPost) {
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

    public List<Post> findByTitle(String keyword) {
        List<Post> posts = postStore.values().stream().toList();
        List<Post> storage = new ArrayList<>();

        for (Post post : posts) {
            if (post.getTitle().contains(keyword)) {
                storage.add(post);
            }
        }

        return storage;
    }

    public List<Post> findByContent(String keyword) {
        List<Post> posts = postStore.values().stream().toList();
        List<Post> storage = new ArrayList<>();

        for (Post post : posts) {
            if (post.getContent().contains(keyword)) {
                storage.add(post);
            }
        }

        return storage;
    }

    public List<Post> findByWriter(String keyword) {
        List<Post> posts = postStore.values().stream().toList();
        List<Post> storage = new ArrayList<>();

        for (Post post : posts) {
            if (post.getUsername().contains(keyword)) {
                storage.add(post);
            }
        }

        return storage;
    }

    public int getCount() {
        return postStore.values().size();
    }
}
