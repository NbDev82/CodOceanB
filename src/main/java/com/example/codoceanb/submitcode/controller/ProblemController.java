package com.example.codoceanb.submitcode.controller;
import com.example.codoceanb.submitcode.DTO.PickOneDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import com.example.codoceanb.submitcode.request.AddProblemRequest;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/findById")
    public ResponseEntity<ProblemDTO> fetchProblem(UUID problemId) {
        ProblemDTO problemDTO = problemService.findById(problemId, ProblemDTO.class);
        log.info("Fetching problem by id: {}", problemId);
        return ResponseEntity.ok(problemDTO);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProblemDTO>> fetchAll() {
        return ResponseEntity.ok(problemService.getAllDTOs());
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addProblem(@RequestBody @NonNull AddProblemRequest request) {
        return ResponseEntity.ok(problemService.add(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteProblem(@RequestParam UUID problemId) {
        try {
            return ResponseEntity.ok(problemService.delete(problemId));
        } catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/pickOne")
    public ResponseEntity<PickOneDTO> pickProblem() {
        Problem problem = problemService.getRandomProblem();
        PickOneDTO dto = new PickOneDTO(problem.getId(), problem.getTitle());
        log.info("Fetching random problem");
        return ResponseEntity.ok(dto);
    }
}
