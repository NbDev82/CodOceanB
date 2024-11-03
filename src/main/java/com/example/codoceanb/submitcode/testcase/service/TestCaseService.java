package com.example.codoceanb.submitcode.testcase.service;

import com.example.codoceanb.submitcode.DTO.PublicTestCaseDTO;

import java.util.List;
import java.util.UUID;

public interface TestCaseService {
    List<PublicTestCaseDTO> getPublicTestCases(UUID problemId);
}
