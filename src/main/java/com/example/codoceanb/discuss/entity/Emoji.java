package com.example.codoceanb.discuss.entity;

import com.example.codoceanb.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "emojis")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(EmojiId.class)
public class Emoji {
    @Id
    @ManyToOne
    private User owner;
    @Id
    @ManyToOne
    private Discuss discuss;
}
