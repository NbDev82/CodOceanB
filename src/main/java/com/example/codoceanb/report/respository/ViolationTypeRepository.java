package com.example.codoceanb.report.respository;

import com.example.codoceanb.report.entity.ViolationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ViolationTypeRepository extends JpaRepository<ViolationType, UUID> {
}
