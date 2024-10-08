package com.example.codoceanb.submitcode.parameter.repository;

import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, UUID> {
}
