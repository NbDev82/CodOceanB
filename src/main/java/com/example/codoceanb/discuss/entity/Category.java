package com.example.codoceanb.discuss.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String description;
    private String imageUrl;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Discuss> discusses;
}
