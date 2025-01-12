package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.jdbc.Comment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class CommentMemoryRepositoryTest {

    CommentMemoryRepository commentRepository = new CommentMemoryRepository();

    @Test
    void all() {
        Comment comment1 = new Comment(null, 1L, "test", "testComment", LocalDateTime.now());
        Comment comment2 = new Comment(null, 1L, "test", "testComment", LocalDateTime.now());

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        assertThat(commentRepository.findByPostId(1L).size()).isEqualTo(2);

        Comment updateComment = new Comment(null, 1L, "test", "testCommentMOD", LocalDateTime.now());

        log.info("before update comment={}", comment1);

        commentRepository.modify(comment1.getId(), updateComment.getContent());

        log.info("after update comment={}", comment1);

        assertThat(comment1.getContent()).isEqualTo("testCommentMOD");

        commentRepository.delete(comment1.getId());

        assertThat(commentRepository.findByPostId(1L).size()).isEqualTo(1);
    }
}