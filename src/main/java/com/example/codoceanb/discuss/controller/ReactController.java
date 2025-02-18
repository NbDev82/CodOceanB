package com.example.codoceanb.discuss.controller;

import com.example.codoceanb.discuss.request.EmojiRequest;
import com.example.codoceanb.discuss.service.EmojiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/react/discuss")
@RequiredArgsConstructor
public class ReactController {

    @Autowired
    private EmojiService emojiService;

    @PostMapping
    public ResponseEntity<Boolean> addEmoji(@RequestBody EmojiRequest emojiRequest,
                                           @RequestHeader("Authorization") String authHeader) {
        emojiService.addEmoji(authHeader, emojiRequest.getDiscussId());
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteEmoji(@PathVariable(name = "id") UUID discussId,
                                              @RequestHeader("Authorization") String authHeader) {
        emojiService.deleteEmoji(authHeader, discussId);
        return ResponseEntity.ok(true);
    }
}
