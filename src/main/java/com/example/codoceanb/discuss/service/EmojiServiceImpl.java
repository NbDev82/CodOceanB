package com.example.codoceanb.discuss.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.discuss.entity.Discuss;
import com.example.codoceanb.discuss.entity.Emoji;
import com.example.codoceanb.discuss.entity.EmojiId;
import com.example.codoceanb.discuss.repository.EmojiRepository;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmojiServiceImpl implements EmojiService{

    @Autowired
    private EmojiRepository emojiRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscussService discussService;

    @Override
    public void addEmoji(String authHeader, UUID discussId) {
        User owner = userService.getUserDetailsFromToken(authHeader);
        Discuss discuss = discussService.getDiscuss(discussId);
        EmojiId id = EmojiId.builder()
                .owner(owner.getId())
                .discuss(discussId)
                .build();

        if(!emojiRepository.existsById(id)) {
            Emoji emoji = Emoji.builder()
                    .owner(owner)
                    .discuss(discuss)
                    .build();
            emojiRepository.save(emoji);
        }
    }

    @Override
    public void deleteEmoji(String authHeader, UUID discussId) {
        User owner = userService.getUserDetailsFromToken(authHeader);
        EmojiId id = EmojiId.builder()
                .owner(owner.getId())
                .discuss(discussId)
                .build();

        emojiRepository.deleteById(id);
    }
}
