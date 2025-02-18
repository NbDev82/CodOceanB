package com.example.codoceanb.discuss.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmojiId implements Serializable {
    private UUID owner;
    private UUID discuss;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmojiId emojiId = (EmojiId) o;
        return Objects.equals(owner, emojiId.owner) && Objects.equals(discuss, emojiId.discuss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, discuss);
    }
}
