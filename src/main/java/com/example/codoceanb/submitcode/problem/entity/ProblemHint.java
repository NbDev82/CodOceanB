package com.example.codoceanb.submitcode.problem.entity;

import com.example.codoceanb.submitcode.DTO.ProblemApproachDTO;
import com.example.codoceanb.submitcode.DTO.ProblemHintDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "problem_hints")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemHint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "is_locked")
    private boolean isLocked;

    @Column(name = "over_view", columnDefinition = "text")
    private String overView;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProblemApproach> approaches;

    @Column(name = "pseudo_code", columnDefinition = "text")
    private String pseudoCode;

    @OneToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public ProblemHintDTO toDTO() {
        List<ProblemApproachDTO> problemApproachDTOs = getApproaches().stream().map(
                ProblemApproach::toDTO
        ).toList();

        return ProblemHintDTO.builder()
                .isLocked(isLocked)
                .overView(overView)
                .pseudoCode(pseudoCode)
                .approachDTOs(problemApproachDTOs)
                .build();
    }
}
