package com.example.codoceanb.discuss.comment.repository;

import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.discuss.comment.dto.DiscussCommentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscussCommentRepository extends JpaRepository<Comment, UUID> {

    @Query(value =  "SELECT c " +
                    "FROM Comment c " +
                    "INNER JOIN Discuss d ON d.id = c.discuss.id " +
                    "WHERE d.id = :discussId " +
                    "AND c.isDeleted = false " +
                    "ORDER BY c.createdAt DESC")
    List<Comment> findAllByDiscussId(UUID discussId);

    @Query(value =  "SELECT c " +
                    "FROM Comment c " +
                    "WHERE c.commentParent.isDeleted = false " +
                    "AND c.commentParent.id = :commentId " +
                    "AND c.isDeleted = false " +
                    "ORDER BY c.createdAt DESC")
    List<Comment> findByCommentParentId(UUID commentId);
}
