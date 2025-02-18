package com.example.codoceanb.comment.repository;

import com.example.codoceanb.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query("SELECT c " +
            "FROM Comment c " +
            "WHERE c.problem.id = :problemId")
    List<Comment> findCommentsByProblemId(UUID problemId);
}
