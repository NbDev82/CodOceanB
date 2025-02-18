package com.example.codoceanb.submitcode.strategy;

import com.example.codoceanb.submitcode.DTO.CustomTestCaseDTO;
import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;

import java.util.List;

public class CompilerProcessor {
    private final CompilerStrategy compilerStrategy;

    public CompilerProcessor(CompilerStrategy compilerStrategy) {
        this.compilerStrategy = compilerStrategy;
    }

    public ResultDTO run(String fileLink, String fileName, String code,
                         Problem problem) {
        return compilerStrategy.run(fileLink, fileName, code, problem);
    }

    public ResultDTO runWithCustomTestcase(String fileLink, String fileName, List<CustomTestCaseDTO> customTestCaseDTOs, String code, Problem problem) {
        return compilerStrategy.runWithCustomTestcase(fileLink, fileName, customTestCaseDTOs, code, problem);
    }
}
