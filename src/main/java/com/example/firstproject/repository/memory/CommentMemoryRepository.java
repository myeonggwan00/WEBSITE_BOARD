package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.jdbc.Comment;
import com.example.firstproject.repository.CommentRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CommentMemoryRepository implements CommentRepository {
    private static final Map<Long, Comment> commentStore = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Comment save(Comment comment) {
        comment.setId(id++);
        comment.setCreatedAt(LocalDateTime.now());

        commentStore.put(comment.getId(), comment);

        return comment;
    }

    @Override
    public void modify(Long commentId, String modifyComment) {
        Comment findComment = findByCommentId(commentId);

        findComment.setContent(modifyComment);
        findComment.setCreatedAt(LocalDateTime.now());
    }

    @Override
    public void delete(Long commentId) {
        commentStore.remove(commentId);
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        Collection<Comment> commentList = commentStore.values();
        return commentList.stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .collect(Collectors.toList());
    }

    @Override
    public Comment findByCommentId(Long commentId) {
        return commentStore.get(commentId);
    }

    public int getTotalCnt() {
        return commentStore.size();
    }

    @Override
    public List<Comment> getReplies() {
        return commentStore.values()
                .stream()
                .filter(comment -> comment.getPostId() != null)
                .collect(Collectors.toList());
    }
}
