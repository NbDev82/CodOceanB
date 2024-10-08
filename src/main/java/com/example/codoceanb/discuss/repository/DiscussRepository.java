package com.example.codoceanb.discuss.repository;

import com.example.codoceanb.discuss.entity.Discuss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscussRepository extends JpaRepository<Discuss, UUID> {
    List<Discuss> findByOwnerEmail(String email);
    @Query(value = "SELECT d.id, d.category_id, d.content, d.created_at, d.description, d.end_at, d.image, d.user_id, d.problem_id, d.topic, d.updated_at, COUNT(c.id) AS comment_count " +
                   "FROM discusses d " +
                   "LEFT JOIN comments c ON d.id = c.discuss_id " +
                   "WHERE (:searchTerm IS NULL OR d.content LIKE %:searchTerm%) " +
                   "AND (:category IS NULL OR d.category_id = :category) " +
                   "GROUP BY d.id, d.category_id, d.content, d.created_at, d.description, d.end_at, d.image, d.user_id, d.problem_id, d.topic, d.updated_at " +
                   "ORDER BY comment_count DESC",
           nativeQuery = true)
    Page<Discuss> findAllWithCommentCount(@Param("searchTerm") String searchTerm,
                                          @Param("category") String category,
                                          Pageable pageable);
}
