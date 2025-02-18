package com.example.codoceanb.submitcode.library.repository;

import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LibraryRepository extends JpaRepository<LibrariesSupport, UUID> {
}
