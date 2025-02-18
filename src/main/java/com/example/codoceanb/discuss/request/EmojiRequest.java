package com.example.codoceanb.discuss.request;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmojiRequest {
    private UUID discussId;
}
