package com.example.codoceanb.profile.controller;

import com.example.codoceanb.profile.dto.TestCaseDTO;
import com.example.codoceanb.profile.request.UpdateTestCaseRequest;
import com.example.codoceanb.submitcode.DTO.AddTestCaseRequestDTO;
import com.example.codoceanb.submitcode.DTO.ProblemHintDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.service.ProfileProblemService;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile-problem")
@RequiredArgsConstructor
public class ProfileProblemController {
    @Autowired
    private final ProfileProblemService profileProblemService;
    @PutMapping("/title/{problemId}")
    public ResponseEntity<Void> updateTitle(@PathVariable UUID problemId, @RequestBody String title) {
        profileProblemService.updateTitle(problemId, title);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/difficulty/{problemId}")
    public ResponseEntity<Void> updateDifficulty(@PathVariable UUID problemId, @RequestBody Problem.EDifficulty difficulty) {
        profileProblemService.updateDifficulty(problemId, difficulty); 
        return ResponseEntity.ok().build();
    }

    @PutMapping("/description/{problemId}") 
    public ResponseEntity<Void> updateDescription(@PathVariable UUID problemId, @RequestBody String description) {
        profileProblemService.updateDescription(problemId, description);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/hint/{problemId}")
    public ResponseEntity<Void> updateProblemHint(@PathVariable UUID problemId, @RequestBody ProblemHintDTO problemHintDTO) {
        profileProblemService.updateProblemHint(problemId, problemHintDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/testcases/{problemId}")
    public ResponseEntity<List<TestCaseDTO>> getTestCases(@PathVariable UUID problemId) {
        List<TestCaseDTO> testCases = profileProblemService.getTestCases(problemId);
        return ResponseEntity.ok(testCases);
    }

    @PutMapping("/testcases/{problemId}")
    public ResponseEntity<Void> updateTestCases(@PathVariable UUID problemId, @RequestBody UpdateTestCaseRequest request) {
        profileProblemService.updateTestCases(problemId, request.getTestcases());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/hint/{problemId}/lock")
    public ResponseEntity<Void> lockHint(@PathVariable UUID problemId) {
        profileProblemService.lockHint(problemId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/hint/{problemId}/unlock") 
    public ResponseEntity<Void> unlockHint(@PathVariable UUID problemId) {
        profileProblemService.unlockHint(problemId);
        return ResponseEntity.ok().build();
    }
}
