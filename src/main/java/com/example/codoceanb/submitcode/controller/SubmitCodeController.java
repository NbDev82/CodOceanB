package com.example.codoceanb.submitcode.controller;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.exception.ProblemNotFoundException;
import com.example.codoceanb.submitcode.exception.UnsupportedLanguageException;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
import com.example.codoceanb.submitcode.request.SubmitCodeRequest;
import com.example.codoceanb.submitcode.request.TestCodeWithCustomTestcaseRequest;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.submission.service.SubmissionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/code")
public class SubmitCodeController {
    private static final Logger log = LogManager.getLogger(SubmitCodeController.class);

    private final SubmissionService submissionService;
    private final ProblemRepository problemRepository;

    @Autowired
    public SubmitCodeController(SubmissionService submissionService, ProblemRepository problemRepository) {
        this.submissionService = submissionService;
        this.problemRepository = problemRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<ResultDTO> submitCode(@RequestBody SubmitCodeRequest request, @RequestHeader(value = "Authorization") String authHeader) {
        Problem problem = findProblemOrThrow(request.getProblemId());
        Submission.ELanguage language = parseLanguage(request.getLanguage());
        
        ResultDTO resultDTO = submissionService.runCode(authHeader, request.getCode(), problem, language);
        return ResponseEntity.ok(resultDTO);
    }

    @PostMapping("/test-code-with-custom-testcases")
    public ResponseEntity<ResultDTO> testWithCustomTestCases(@RequestBody TestCodeWithCustomTestcaseRequest request, @RequestHeader(value = "Authorization") String authHeader) {
        ResultDTO resultDTO = submissionService.testWithCustomTestCases(authHeader, request);
        return ResponseEntity.ok(resultDTO);
    }

    @PostMapping("/compile")
    public ResponseEntity<ResultDTO> compileCode(@RequestBody SubmitCodeRequest request, @RequestHeader(value = "Authorization") String authHeader) {
        Submission.ELanguage language = parseLanguage(request.getLanguage());
        ResultDTO resultDTO = submissionService.compile(authHeader, request.getCode(), language);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("/input")
    public ResponseEntity<String> getInputCode(@RequestParam UUID problemId, @RequestParam String language) {
        Problem problem = findProblemOrThrow(problemId);
        Submission.ELanguage eLanguage = parseLanguage(language);
        String inputCode = submissionService.getInputCode(problem, eLanguage);
        return ResponseEntity.ok(inputCode);
    }

    private Problem findProblemOrThrow(UUID problemId) {
        return problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
    }

    private Submission.ELanguage parseLanguage(String language) {
        try {
            return Submission.ELanguage.valueOf(language.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedLanguageException("Language is not supported yet!");
        }
    }
}
