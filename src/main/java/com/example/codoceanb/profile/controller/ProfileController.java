package com.example.codoceanb.profile.controller;

import com.example.codoceanb.discuss.service.DiscussService;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.profile.request.ChangeEmailRequest;
import com.example.codoceanb.profile.response.ProfileResponse;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private static final Logger log = LogManager.getLogger(ProfileController.class);

    private final UserService userService;
    private final ProblemService problemService;
    private final DiscussService discussService;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/get-profile")
    public ResponseEntity<ProfileResponse> extractProfile(@RequestHeader(value = "Authorization") String authHeader) {
        ProfileResponse profileResponse = userService.getProfile(authHeader);
        return ResponseEntity.ok(profileResponse);
    }

    @PostMapping("/change-profile")
    public ResponseEntity<ProfileResponse> changeProfile(@RequestBody ProfileDTO profileDTO,
                                                         @RequestHeader(value = "Authorization") String authHeader) {
        return ResponseEntity.ok(userService.changeProfile(authHeader, profileDTO));
    }

//    @GetMapping("/collect-user-info")
//    public ResponseEntity<ProfileDTO> collectUserInfo(@RequestHeader(value = "Authorization") String authHeader) {
//        ProfileDTO profileDTO = userService.collectUserInfo(authHeader);
//        return ResponseEntity.ok(profileDTO);
//    }

    @PostMapping("/change-email")
    public ResponseEntity<ProfileResponse> changeEmail(@RequestBody ChangeEmailRequest request,
                                                       @RequestHeader(value = "Authorization") String authHeader) {
        ProfileResponse profileResponse = userService.changeEmail(authHeader, request.getOtp(), request.getNewEmail());
        return ResponseEntity.ok(profileResponse);
    }
    @GetMapping("/get-all-uploaded-problems")
    public ResponseEntity<List<ProblemDTO>> getProblemsByOwner(@RequestHeader(value = "Authorization") String authHeader) {
        log.info("Fetching problems by token: {}", authHeader);
        return ResponseEntity.ok(problemService.getAllUploadedProblemsByUser(authHeader));
    }

    @GetMapping("/get-all-solved-problems")
    public ResponseEntity<List<ProblemDTO>> getAllSolvedProblems(@RequestHeader(value = "Authorization") String authHeader) {
        log.info("Fetching all solved problems by token: {}", authHeader);
        return ResponseEntity.ok(problemService.getAllSolvedProblemsByUser(authHeader));
    }

    @GetMapping("/get-all-uploaded-discusses")
    public ResponseEntity<List<DiscussDTO>> getAllUploadedDiscusses(@RequestHeader(value = "Authorization") String authHeader) {
        log.info("Fetching all uploaded discusses by token: {}", authHeader);
        return ResponseEntity.ok(discussService.getAllUploadedDiscussesByUser(authHeader));
    }
}
