package com.example.codoceanb.submitcode.problem.entity;
import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.contest.entity.Contest;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "problems")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Problem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "function_name")
    private String functionName;

    @Column(name = "output_datatype")
    private String outputDataType;

    @Enumerated(EnumType.STRING)
    private EDifficultyLevel difficultyLevel;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = ETopic.class)
    private List<ETopic> topics;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contest_problem",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "contest_id"))
    private List<Contest> contests;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<LibrariesSupport> librariesSupports;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<TestCase> testCases;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<Submission> submissions;

    public enum EDifficultyLevel {
        EASY, NORMAL, HARD;
    }

    public enum ETopic {
        DIVIDE_AND_CONQUER,
        TREE,
        GRAPH,
        DYNAMIC_PROGRAMMING,
        BIT_MANIPULATION,
        BACKTRACKING,
        STRING,
        ARRAY,
        SORTING,
        MATH,
        GREEDY,
        RECURSION,
    }
}
