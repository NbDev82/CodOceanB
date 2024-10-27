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

    @GetMapping("/{id}")
    public ResponseEntity<ProblemDTO> fetchProblem(@PathVariable UUID id) {
        ProblemDTO problemDTO = problemService.findById(id, ProblemDTO.class);
        log.info("Fetching problem by id: {}", id);
        return ResponseEntity.ok(problemDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProblemDTO>> fetchAll() {
        return ResponseEntity.ok(problemService.getAllDTOs());
    }

    @PostMapping
    public ResponseEntity<Boolean> addProblem(@RequestBody @NonNull AddProblemRequest request,
                                              @RequestHeader(name = "Authorization") String authHeader) {
        return ResponseEntity.ok(problemService.add(request, authHeader));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProblem(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(problemService.delete(id));
        } catch (Exception e){
            log.info(e.getMessage());
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/random")
    public ResponseEntity<PickOneDTO> pickProblem() {
        Problem problem = problemService.getRandomProblem();
        PickOneDTO dto = new PickOneDTO(problem.getId(), problem.getTitle());
        log.info("Fetching random problem");
        return ResponseEntity.ok(dto);
    }
}
