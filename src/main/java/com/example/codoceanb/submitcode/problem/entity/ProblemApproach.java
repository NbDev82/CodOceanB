package com.example.codoceanb.submitcode.problem.entity;

import com.example.codoceanb.submitcode.DTO.ProblemApproachDTO;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "problem_approaches")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemApproach {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    //"intuition" ám chỉ ý tưởng chính hoặc trực giác đằng sau cách tiếp cận
    // được đề xuất để giải quyết bài toán, chứ không phải là bước triển khai chi tiết.
    // Nó tập trung vào việc giải thích cách tiếp cận ở mức khái niệm
    // và lý do tại sao cách tiếp cận này lại hợp lý hoặc hiệu quả.

    //intuition ở đây là việc cung cấp một cách nhìn rõ ràng,
    // cấp cao về cách giải quyết bài toán, giúp người đọc hiểu được ý tưởng
    // và cơ sở logic của thuật toán trước khi đi sâu vào chi tiết triển khai.
    @Column(columnDefinition = "text")
    private String intuition;


    //Algorithm ám chỉ phần mô tả chi tiết các bước thực hiện để giải quyết bài toán
    // dựa trên "intuition" đã nêu trước đó.

    // Đây là phần chuyển đổi từ ý tưởng (intuition) thành các bước cụ thể
    // có thể thực hiện bằng máy tính.
    @Column(columnDefinition = "text")
    private String algorithm;

    //Implementation là phần hiện thực hóa thuật toán (Algorithm)
    // thành mã nguồn (code) cụ thể, có thể chạy được trên máy tính.
    // Đây là bước cuối cùng trong quá trình từ intuition → algorithm → implementation,
    // nơi ý tưởng và quy trình được chuyển thành cú pháp lập trình rõ ràng.
    @Column(columnDefinition = "text")
    private String implementation;

    private Submission.ELanguage language;

    @ManyToOne
    @JoinColumn(name = "problem_hint_id")
    private ProblemHint problemHint;

    public ProblemApproachDTO toDTO() {
        return ProblemApproachDTO.builder()
                .intuition(intuition)
                .algorithm(algorithm)
                .implementation(implementation)
                .language(language)
                .build();
    }
}
