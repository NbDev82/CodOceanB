package com.example.codoceanb.submitcode.controller;

import com.example.codoceanb.submitcode.DTO.SubmissionDTO;
import com.example.codoceanb.submitcode.submission.service.SubmissionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
    private static final Logger log = LogManager.getLogger(SubmissionController.class);

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping("/gets")
    public ResponseEntity<List<SubmissionDTO>> getSubmissions(Long userId, Long problemId) {
        List<SubmissionDTO> submissionDTOs = submissionService.getByUserIdAndProblemId(userId,problemId);
        log.info("Retrieved submissions from SubmissionController");
        return ResponseEntity.ok(submissionDTOs);
    }
}
