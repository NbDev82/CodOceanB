package com.example.codoceanb.submitcode.controller;
import com.example.codoceanb.submitcode.DTO.PickOneDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.DTO.PublicTestCaseDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import com.example.codoceanb.submitcode.request.AddProblemRequest;
import com.example.codoceanb.submitcode.testcase.service.TestCaseService;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {
    private static final Logger log = LogManager.getLogger(ProblemController.class);

    @Autowired
    private ProblemService problemService;

    @Autowired
    private TestCaseService testCaseService;

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDTO> fetchProblem(@PathVariable UUID id) {
        try {
            ProblemDTO problemDTO = problemService.findById(id, ProblemDTO.class);
            log.info("Fetching problem by id: {}", id);
            return ResponseEntity.ok(problemDTO);
        } catch (Exception e) {
            log.error("Error fetching problem by id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProblemDTO>> fetchAll() {
        try {
            return ResponseEntity.ok(problemService.getAllDTOs());
        } catch (Exception e) {
            log.error("Error fetching all problems", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Boolean> addProblem(@RequestBody @NonNull AddProblemRequest request,
                                              @RequestHeader(name = "Authorization") String authHeader) {
        try {
            return ResponseEntity.ok(problemService.add(request, authHeader));
        } catch (Exception e) {
            log.error("Error adding problem", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProblem(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(problemService.delete(id));
        } catch (Exception e) {
            log.error("Error deleting problem by id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/random")
    public ResponseEntity<PickOneDTO> pickProblem() {
        try {
            Problem problem = problemService.getRandomProblem();
            PickOneDTO dto = new PickOneDTO(problem.getId(), problem.getTitle());
            log.info("Fetching random problem");
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error fetching random problem", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/public-tc")
    public ResponseEntity<List<PublicTestCaseDTO>> getPublicTestcases(@PathVariable(name = "id") UUID problemId) {
        try {
            return ResponseEntity.ok(testCaseService.getPublicTestCases(problemId));
        } catch (Exception e) {
            log.error("Error fetching public test cases for problem ID: {}", problemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/correct-answer")
    public ResponseEntity<String> getCorrectAnswer(@PathVariable(name = "id") UUID problemId) {
        try {
            return ResponseEntity.ok(problemService.getCorrectAnswer(problemId));
        } catch (Exception e) {
            log.error("Error fetching correct answer for problem ID: {}", problemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
