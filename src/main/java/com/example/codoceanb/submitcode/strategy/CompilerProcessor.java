package com.example.codoceanb.submitcode.strategy;

import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;

public class CompilerProcessor {
    private final CompilerStrategy compilerStrategy;

    public CompilerProcessor(CompilerStrategy compilerStrategy) {
        this.compilerStrategy = compilerStrategy;
    }

    public ResultDTO run(String fileLink, String fileName, String code,
                         Problem problem) {
        return compilerStrategy.run(fileLink, fileName, code, problem);
    }
}
