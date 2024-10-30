package com.example.codoceanb.discuss.repository;

import com.example.codoceanb.discuss.entity.Emoji;
import com.example.codoceanb.discuss.entity.EmojiId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmojiRepository extends JpaRepository<Emoji, EmojiId> {
}
