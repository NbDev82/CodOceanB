package com.example.codoceanb.discuss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "img_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "discuss_id")
    private Discuss discuss;
}
