package com.example.codoceanb.submitcode.controller;

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

@RestController
@RequestMapping("/api/submit-code")
public class SubmitCodeController {
    private static final Logger log = LogManager.getLogger(SubmitCodeController.class);

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ProblemRepository problemRepos;

    @PostMapping("/run")
    public ResponseEntity<ResultDTO> submitCode(@RequestBody SubmitCodeRequest request) {
        Long userId = request.getUserId();
        String code = request.getCode();
        Problem problem = problemRepos.findById(request.getProblemId()).orElse(null);
        Submission.ELanguage eLanguage;

        try{
            eLanguage = Submission.ELanguage.valueOf(request.getLanguage().toUpperCase());
        } catch (IllegalArgumentException e){
            throw new UnsupportedLanguageException("Language is not supported yet!");
        }

        ResultDTO resultDTO =  submissionService.runCode(userId, code, problem, eLanguage);
        return ResponseEntity.ok(resultDTO);
    }

    @PostMapping("/compile")
    public ResponseEntity<ResultDTO> compileCode(@RequestBody SubmitCodeRequest request) {
        String code = request.getCode();
        Submission.ELanguage eLanguage;

        try{
            eLanguage = Submission.ELanguage.valueOf(request.getLanguage().toUpperCase());
        } catch (IllegalArgumentException e){
            throw new UnsupportedLanguageException("Language is not supported yet!");
        }

        ResultDTO resultDTO = submissionService.compile(code, eLanguage);
        return ResponseEntity.ok(resultDTO);
    }

    @GetMapping("/getInputCode")
    public ResponseEntity<String> getInputCode(Long problemId, String language) {
        Submission.ELanguage eLanguage;

        try{
            eLanguage = Submission.ELanguage.valueOf(language.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new UnsupportedLanguageException("Language is not supported yet!");
        }

        Problem problem = problemRepos.findById(problemId).orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
        String inputCode = submissionService.getInputCode(problem,eLanguage);
        return ResponseEntity.ok(inputCode);
    }
}
