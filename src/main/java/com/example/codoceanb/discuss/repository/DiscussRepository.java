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
    @Query(value = "SELECT d.id, d.title, d.created_at, d.description, d.end_at, d.is_closed, d.image, d.user_id, d.updated_at, COUNT(c.id) AS comment_count, u.url_img AS user_url_img, u.full_name AS user_name, u.email AS user_email " +
            "FROM discusses d " +
            "LEFT JOIN comments c ON d.id = c.discuss_id " +
            "LEFT JOIN discuss_categories dc ON d.id = dc.discuss_id " +
            "LEFT JOIN categories cat ON dc.category_id = cat.id " +
            "LEFT JOIN users u ON d.user_id = u.id " +
            "WHERE d.is_closed IS FALSE " +
            "AND (:searchTerm IS NULL OR d.title LIKE %:searchTerm%) " +
            "AND (:category IS NULL OR cat.name = :category) " +
            "GROUP BY d.id, d.title, d.created_at, d.description, d.end_at, d.is_closed, d.image, d.updated_at, u.id, u.url_img, u.full_name, u.email " +
            "ORDER BY comment_count DESC",
            nativeQuery = true)
    Page<Discuss> findAllWithCommentCount(@Param("searchTerm") String searchTerm,
                                          @Param("category") String category,
                                          Pageable pageable);
}
