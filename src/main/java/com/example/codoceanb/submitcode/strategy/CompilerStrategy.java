package com.example.codoceanb.submitcode.strategy;

import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;

import java.util.List;

public interface CompilerStrategy {
    String createInputCode(Problem problem, String code, TestCase testCase);
    void writeFile(String fileLink, String fileName, String code);
    void deleteFileCompiled(String fileLink, String fileName);
    CompilerResult compile(String code, String fileLink, String fileName);
    ResultDTO run(String fileLink, String fileName, String code, Problem problem);
}
