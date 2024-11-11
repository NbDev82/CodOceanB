package com.example.codoceanb.discuss.controller;

import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.discuss.request.AddDiscussRequest;
import com.example.codoceanb.discuss.request.UpdateDiscussRequest;
import com.example.codoceanb.discuss.response.DiscussResponse;
import com.example.codoceanb.discuss.service.DiscussService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/discusses")
@RequiredArgsConstructor
public class DiscussController {

    private final DiscussService discussService;

    @GetMapping
    public ResponseEntity<DiscussResponse> getDiscusses(@RequestParam(defaultValue = "0") int pageNumber,
                                                        @RequestParam(defaultValue = "10") int limit,
                                                        @RequestParam(defaultValue = "") String searchTerm,
                                                        @RequestParam(required = false) String category,
                                                        @RequestHeader("Authorization") String authHeader) {
        List<DiscussDTO> discussDTOs = discussService.getDiscusses(authHeader, pageNumber, limit, searchTerm, category);
        return ResponseEntity.ok(new DiscussResponse(discussDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscussDTO> getDiscuss(@PathVariable UUID id,
                                                 @RequestHeader("Authorization") String authHeader) {
        DiscussDTO discussDTO = discussService.getDiscussById(id, authHeader);
        return ResponseEntity.ok(discussDTO);
    }

    @PostMapping
    public ResponseEntity<DiscussDTO> addDiscuss(@RequestPart("request") AddDiscussRequest request,
                                                 @RequestHeader(value = "Authorization") String authHeader,
                                                 @RequestPart(value = "multipartFiles", required = false) List<MultipartFile> multipartFiles) {
        request.setMultipartFiles(multipartFiles);
        return ResponseEntity.ok(discussService.addDiscuss(request, authHeader));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DiscussDTO> updateDiscuss(@PathVariable UUID id,
                                                    @RequestBody UpdateDiscussRequest request) {
        return ResponseEntity.ok(discussService.updateDiscuss(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscuss(@PathVariable UUID id) {
        discussService.deleteDiscuss(id);
        return ResponseEntity.ok().build();
    }
}
