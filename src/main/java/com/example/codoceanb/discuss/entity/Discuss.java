package com.example.codoceanb.discuss.entity;

import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "discusses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Discuss implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 100000000)
    private String title;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "discuss_categories",
            joinColumns = @JoinColumn(name = "discuss_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "discuss", fetch = FetchType.LAZY)
    private List<Comment> comments;
}
