package com.example.codoceanb.profile.controller;

import com.example.codoceanb.discuss.service.DiscussService;
import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.request.ChangeEmailRequest;
import com.example.codoceanb.profile.response.ProfileResponse;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private static final Logger log = LogManager.getLogger(ProfileController.class);

    @Autowired
    private final UserService userService;
    @Autowired
    private final ProblemService problemService;
    @Autowired
    private final DiscussService discussService;
    @GetMapping("/get-profile")
    public ResponseEntity<ProfileResponse> extractProfile(@RequestHeader(value = "Authorization") String token) {
        token = token.substring(7);
        ProfileResponse profileResponse = userService.getProfile(token);
        return ResponseEntity.ok(profileResponse);
    }

    @PostMapping("/change-profile")
    public ResponseEntity<ProfileResponse> changeProfile(@RequestBody ProfileDTO profileDTO,
                                                         @RequestHeader(value = "Authorization") String token) {
        token = token.substring(7);
        ProfileResponse profileResponse = userService.changeProfile(token, profileDTO);
        return ResponseEntity.ok(profileResponse);
    }

    @PostMapping("/change-email")
    public ResponseEntity<ProfileResponse> changeEmail(@RequestBody ChangeEmailRequest request,
                                                       @RequestHeader(value = "Authorization") String token) {
        token = token.substring(7);
        ProfileResponse profileResponse = userService.changeEmail(token, request.getOtp(), request.getNewEmail());
        return ResponseEntity.ok(profileResponse);
    }
    @GetMapping("/get-all-uploaded-problems")
    public ResponseEntity<List<ProblemDTO>> getProblemsByOwner(@RequestHeader(value = "Authorization", required = true) String token) {
        log.info("Fetching problems by token: {}", token);
        
        token = token.substring(7);

        return ResponseEntity.ok(problemService.getAllUploadedProblemsByUser(token));
    }

    @GetMapping("/get-all-solved-problems")
    public ResponseEntity<List<ProblemDTO>> getAllSolvedProblems(@RequestHeader(value = "Authorization") String token) {
        log.info("Fetching all solved problems by token: {}", token);

        token = token.substring(7);

        return ResponseEntity.ok(problemService.getAllSolvedProblemsByUser(token));
    }

    @GetMapping("/get-all-uploaded-discusses")
    public ResponseEntity<List<DiscussDTO>> getAllUploadedDiscusses(@RequestHeader(value = "Authorization") String token) {
        log.info("Fetching all uploaded discusses by token: {}", token);

        token = token.substring(7);

        return ResponseEntity.ok(discussService.getAllUploadedDiscussesByUser(token));
    }
}
