package com.example.codoceanb.submitcode.strategy;

import com.example.codoceanb.submitcode.ECompilerConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompilerResult {
    private ECompilerConstants compilerConstants;
    private String error;

    public CompilerResult(ECompilerConstants compilerConstants) {
        this.compilerConstants = compilerConstants;
    }
}
