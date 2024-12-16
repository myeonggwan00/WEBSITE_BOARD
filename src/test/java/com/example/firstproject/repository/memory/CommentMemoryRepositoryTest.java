package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.Comment;
import com.example.firstproject.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CommentMemoryRepositoryTest {

    CommentMemoryRepository commentRepository = new CommentMemoryRepository();

    @Test
    void all() {
        Comment comment1 = new Comment(null, 1L, "test", "testComment", LocalDateTime.now());
        Comment comment2 = new Comment(null, 1L, "test", "testComment", LocalDateTime.now());

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        assertThat(commentRepository.findByPostBno(1L).size()).isEqualTo(2);

        Comment updateComment = new Comment(null, 1L, "test", "testCommentMOD", LocalDateTime.now());

        log.info("before update comment={}", comment1);

        commentRepository.modify(comment1.getCno(), updateComment.getContent());

        log.info("after update comment={}", comment1);

        assertThat(comment1.getContent()).isEqualTo("testCommentMOD");

        commentRepository.delete(comment1.getCno());

        assertThat(commentRepository.findByPostBno(1L).size()).isEqualTo(1);
    }
}