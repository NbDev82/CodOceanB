package com.example.codoceanb.submitcode.testcase.service;

import com.example.codoceanb.submitcode.DTO.ParameterDTO;
import com.example.codoceanb.submitcode.DTO.PublicTestCaseDTO;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import com.example.codoceanb.submitcode.testcase.repository.TestCaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TestCaseServiceImpl implements TestCaseService{
    private static final Logger log = LogManager.getLogger(TestCaseServiceImpl.class);

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    public List<PublicTestCaseDTO> getPublicTestCases(UUID problemId) {
        log.info("Fetching public test cases for problem ID: {}", problemId);
        return convertPublicTestCaseDTOs(testCaseRepository.getPublicTestCasesByProblemId(problemId));
    }

    private List<PublicTestCaseDTO> convertPublicTestCaseDTOs(List<TestCase> publicTestCases) {
        log.debug("Converting public test cases to DTOs");
        return publicTestCases.stream()
                .map(this::convertPublicTestCaseDTO)
                .collect(ArrayList::new, List::add, List::addAll);
    }

    private PublicTestCaseDTO convertPublicTestCaseDTO(TestCase t) {
        log.debug("Converting test case to DTO: {}", t);
        return PublicTestCaseDTO.builder()
                .parameterDTOs(convertParametersDTO(t.getParameters()))
                .build();
    }

    private List<ParameterDTO> convertParametersDTO(List<Parameter> parameters) {
        log.debug("Converting parameters to DTOs");
        return parameters.stream()
                .map(this::convertParameterDTO)
                .collect(ArrayList::new, List::add, List::addAll);
    }

    private ParameterDTO convertParameterDTO(Parameter p) {
        log.debug("Converting parameter to DTO: {}", p);
        return ParameterDTO.builder()
                .name(p.getName())
                .inputDataType(p.getInputDataType())
                .inputData(p.getInputData())
                .build();
    }
}
