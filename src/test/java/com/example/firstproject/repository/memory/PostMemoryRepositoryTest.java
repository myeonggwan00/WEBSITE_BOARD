//package com.example.firstproject.repository.memory;
//
//import com.example.firstproject.domain.Post;
//import com.example.firstproject.repository.PostRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.*;
//
//@Slf4j
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class PostMemoryRepositoryTest {
//    @Autowired
//    PostRepository postRepository;
//
//
//
//    @Test @Order(1)
//    public void save() {
//        Post post = new Post("save test", "test", "tester");
//
//        postRepository.save(post);
//
//        assertThat(postRepository.getCount()).isEqualTo(151);
//    }
//
//    @Test @Order(2)
//    public void modify() {
//        Post updateBeforePost = postRepository.findByBno(1L).get();
//
//        log.info("before={}", updateBeforePost);
//
//        Post updatePost = new Post("modify test", "test", "tester");
//
//        postRepository.modify(1L, updatePost);
//
//        Post updateAfterPost = postRepository.findByBno(1L).get();
//
//        log.info("after={}", updateAfterPost);
//
//        assertThat(updateAfterPost.getTitle()).isEqualTo("modify test");
//        assertThat(updateAfterPost.getContent()).isEqualTo("test");
//    }
//
//
//    @Test @Order(3)
//    public void findByNo() {
//        Post findPost = postRepository.findByBno(1L).get();
//
//        assertThat(findPost.getTitle()).isEqualTo("modify test");
//        assertThat(findPost.getContent()).isEqualTo("test");
//    }
//
//    @Test @Order(4)
//    public void findAll() {
//        assertThat(postRepository.findAll().size()).isEqualTo(151);
//    }
//
//    @Test @Order(5)
//    public void remove() {
//        postRepository.remove(1L);
//
//        assertThat(postRepository.findAll().size()).isEqualTo(150);
//    }
//
//    @Test @Order(5)
//    public void selectPage() {
//        List<Post> findPost = postRepository.findAll();
//
//        Map<String, Integer> map = new HashMap<>();
//        map.put("offset", 10);
//        map.put("pageSize", 10);
//
//        List<Post> posts = postRepository.selectPage(map, findPost);
//
//        for (Post post : posts) {
//            log.info("post={}", post);
//        }
//
//        assertThat(posts.size()).isEqualTo(10);
//    }
//
//    @Test @Order(6)
//    public void getCount() {
//        assertThat(postRepository.getCount()).isEqualTo(150);
//    }
//
//}
