package com.example.codoceanb.report.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "violation_types")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ViolationType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @Getter
    public enum DiscussViolationType {
        OFF_TOPIC("Content is not relevant to the topic."), // Vi phạm do nội dung không liên quan đến chủ đề.
        INAPPROPRIATE_LANGUAGE("Inappropriate language used."), // Vi phạm do sử dụng ngôn ngữ không phù hợp.
        SPAMMING("Spamming content."), // Vi phạm do gửi thư rác.
        PERSONAL_ATTACKS("Personal attacks."); // Vi phạm do tấn công cá nhân.

        private final String description;
        DiscussViolationType(String description) {
            this.description = description;
        }
    }

    @Getter
    public enum ProblemViolationType {
        DUPLICATE("Duplicate content."), // Vi phạm do trùng lặp.
        OFF_TOPIC("Content is not relevant to the topic."), // Vi phạm do nội dung không liên quan đến chủ đề.
        NOT_A_QUESTION("Not a question."), // Vi phạm do không phải là một câu hỏi.
        TOO_BROAD("Problem is too broad."); // Vi phạm do vấn đề quá rộng.

        private final String description;

        ProblemViolationType(String description) {
            this.description = description;
        }
    }

    @Getter
    public enum CommentViolationType {
        SPAMMING("Spamming content."), // Vi phạm do gửi thư rác.
        OFF_TOPIC("Content is not relevant to the topic."), // Vi phạm do nội dung không liên quan đến chủ đề.
        IMPERSONATION("Impersonation."); // Vi phạm do giả mạo.

        private final String description;

        CommentViolationType(String description) {
            this.description = description;
        }
    }
}
