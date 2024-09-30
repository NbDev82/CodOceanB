package com.example.codoceanb.submitcode.controller;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.exception.ProblemNotFoundException;
import com.example.codoceanb.submitcode.exception.UnsupportedLanguageException;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
import com.example.codoceanb.submitcode.request.SubmitCodeRequest;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.submission.service.SubmissionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/submit-code")
public class SubmitCodeController {
    private static final Logger log = LogManager.getLogger(SubmitCodeController.class);

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ProblemRepository problemRepository;

    @PostMapping("/run")
    public ResponseEntity<ResultDTO> submitCode(@RequestBody SubmitCodeRequest request,
                                                @RequestHeader(value = "Authorization") String authHeader) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
        Submission.ELanguage language = parseLanguage(request.getLanguage());
        
        ResultDTO resultDTO = submissionService.runCode(authHeader, request.getCode(), problem, language);
        return ResponseEntity.ok(resultDTO);
    }

    @PostMapping("/compile")
    public ResponseEntity<ResultDTO> compileCode(@RequestBody SubmitCodeRequest request,
                                                 @RequestHeader(value = "Authorization") String authHeader) {
        Submission.ELanguage language = parseLanguage(request.getLanguage());
        ResultDTO resultDTO = submissionService.compile(authHeader, request.getCode(), language);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("/getInputCode")
    public ResponseEntity<String> getInputCode(@RequestParam UUID problemId, @RequestParam String language) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
        Submission.ELanguage eLanguage = parseLanguage(language);
        String inputCode = submissionService.getInputCode(problem, eLanguage);
        return ResponseEntity.ok(inputCode);
    }

    private Submission.ELanguage parseLanguage(String language) {
        try {
            return Submission.ELanguage.valueOf(language.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedLanguageException("Language is not supported yet!");
        }
    }
}
