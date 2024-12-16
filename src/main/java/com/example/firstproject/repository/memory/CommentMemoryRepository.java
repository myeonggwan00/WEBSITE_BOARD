package com.example.firstproject.repository.memory;

import com.example.firstproject.domain.Comment;
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
    private static Long cno = 0L;

    @Override
    public Comment save(Comment comment) {
        comment.setCno(cno++);
        comment.setRegisterTime(LocalDateTime.now());

        commentStore.put(comment.getCno(), comment);

        return comment;
    }

    @Override
    public void modify(Long cno, String modifyComment) {
        Comment findComment = findByCno(cno);

        findComment.setContent(modifyComment);
        findComment.setRegisterTime(LocalDateTime.now());
    }

    @Override
    public void delete(Long cno) {
        commentStore.remove(cno);
    }

    @Override
    public List<Comment> findByPostBno(Long postBno) {
        Collection<Comment> commentList = commentStore.values();
        return commentList.stream()
                .filter(comment -> comment.getPostBno().equals(postBno))
                .collect(Collectors.toList());
    }

    @Override
    public Comment findByCno(Long cno) {
        return commentStore.get(cno);
    }

    public int getTotalCnt() {
        return commentStore.size();
    }

    @Override
    public List<Comment> getReplies() {
        return commentStore.values()
                .stream()
                .filter(comment -> comment.getPcno() != null)
                .collect(Collectors.toList());
    }
}
