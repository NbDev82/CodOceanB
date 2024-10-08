package com.example.codoceanb.discuss.controller;

import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.discuss.request.AddDiscussRequest;
import com.example.codoceanb.discuss.request.UpdateDiscussRequest;
import com.example.codoceanb.discuss.response.DiscussResponse;
import com.example.codoceanb.discuss.service.DiscussService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/discuss")
@RequiredArgsConstructor
public class DiscussController {

    private final DiscussService discussService;

    @GetMapping("/get-discusses")
    public ResponseEntity<DiscussResponse> getDiscusses(@RequestParam(defaultValue = "0") int pageNumber,
                                                        @RequestParam(defaultValue = "10") int limit,
                                                        @RequestParam(required = false) String searchTerm,
                                                        @RequestParam(required = false) String category) {
        List<DiscussDTO> discussDTOs = discussService.getDiscusses(pageNumber, limit, searchTerm, category);
        return ResponseEntity.ok(new DiscussResponse(discussDTOs));
    }

    @PostMapping("/add")
    public ResponseEntity<DiscussDTO> addDiscuss(@RequestBody AddDiscussRequest request,
                                                 @RequestHeader(value = "Authorization") String authHeader) {
        return ResponseEntity.ok(discussService.addDiscuss(request, authHeader));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DiscussDTO> updateDiscuss(@PathVariable UUID id,
                                                    @RequestBody UpdateDiscussRequest request) {
        return ResponseEntity.ok(discussService.updateDiscuss(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDiscuss(@PathVariable UUID id) {
        discussService.deleteDiscuss(id);
        return ResponseEntity.noContent().build();
    }
}
