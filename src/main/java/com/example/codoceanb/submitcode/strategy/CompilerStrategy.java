package com.example.codoceanb.submitcode.strategy;

import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;

import java.util.List;

public interface CompilerStrategy {
    String createInputCode(Problem problem, String code, TestCase testCase);
    String createRunCode(String code, List<Parameter> parameters, String functionName, String outputDataType);

    void writeFile(String fileName, String code);
    void deleteFileCompiled();
    CompilerResult compile(String code, String fileName);
    ResultDTO run(String code, Problem problem);
}
