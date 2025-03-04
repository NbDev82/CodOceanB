package com.example.codoceanb.submitcode.problem.entity;
import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.contest.entity.Contest;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.statistic.dto.TrendingProblemDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.DTO.ProblemHintDTO;
import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import com.example.codoceanb.topic.dto.TopicDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "problems")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Problem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "function_name")
    private String functionName;

    @Column(name = "correct_answer", columnDefinition = "text")
    private String correctAnswer;

    @Column(name = "output_datatype")
    private String outputDataType;

    @Enumerated(EnumType.STRING)
    private EDifficulty difficulty;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = ETopic.class)
    private List<ETopic> topics;

    @OneToOne(mappedBy = "problem")
    private ProblemHint problemHint;

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

    public enum EDifficulty {
        EASY, NORMAL, HARD;
    }

    @Getter
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

    private ProblemMetrics calculateMetrics() {
        int acceptedCount = this.getSubmissions() != null ?
                (int) this.getSubmissions().stream().filter(s -> s.getStatus().equals(Submission.EStatus.ACCEPTED)).count() : 0;
        int submissionCount = this.getSubmissions() != null ? this.getSubmissions().size() : 0;
        int commentCount = this.getComments() != null ?
                (int) this.getComments().stream().filter(c -> !c.isDeleted()).count() : 0;
        String acceptanceRate = getAcceptanceRate(acceptedCount, submissionCount);

        return new ProblemMetrics(acceptedCount, submissionCount, commentCount, acceptanceRate);
    }

    public ProblemDTO toDTO() {
        ProblemMetrics metrics = calculateMetrics();
        ProblemHintDTO hintDTO = new ProblemHintDTO();
        if(getProblemHint() != null)
            hintDTO = getProblemHint().toDTO();
        return ProblemDTO.builder()
                .id(id)
                .title(title)
                .description(description)
                .difficulty(difficulty.name())
                .outputDataType(outputDataType)
                .problemHintDTO(hintDTO)
                .acceptedCount(metrics.acceptedCount)
                .submissionCount(metrics.submissionCount)
                .discussCount(metrics.commentCount)
                .acceptanceRate(metrics.acceptanceRate)
                .build();
    }

    public TrendingProblemDTO toTrendingDTO() {
        ProblemMetrics metrics = calculateMetrics();
        return TrendingProblemDTO.builder()
                .id(id)
                .title(title)
                .description(description)
                .difficulty(difficulty.name())
                .acceptedCount(metrics.acceptedCount)
                .submissionCount(metrics.submissionCount)
                .discussCount(metrics.commentCount)
                .acceptanceRate(metrics.acceptanceRate)
                .topics(this.getTopics())
                .build();
    }

    private String getAcceptanceRate(int acceptedCount, int submissionCount) {
        double acceptanceRate = ((double) acceptedCount /submissionCount)*100;
        double roundedNumber = Math.round(acceptanceRate * 10.0) / 10.0;
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(roundedNumber);
    }

    private static class ProblemMetrics {
        int acceptedCount;
        int submissionCount;
        int commentCount;
        String acceptanceRate;

        ProblemMetrics(int acceptedCount, int submissionCount, int commentCount, String acceptanceRate) {
            this.acceptedCount = acceptedCount;
            this.submissionCount = submissionCount;
            this.commentCount = commentCount;
            this.acceptanceRate = acceptanceRate;
        }
    }
}
