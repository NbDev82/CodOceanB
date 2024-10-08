package com.example.codoceanb.discuss.repository;

import com.example.codoceanb.discuss.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query(value = "SELECT * FROM categories WHERE name IN :names", nativeQuery = true)
    List<Category> findAllByNames(@Param("names") List<String> names);
}
