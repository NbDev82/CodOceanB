package com.example.codoceanb.comment.entity;

import com.example.codoceanb.discuss.entity.Discuss;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment commentParent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discuss_id")
    private Discuss discuss;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "problem_id")
    private Problem problem;
}
