package com.example.codoceanb.submitcode.testcase.entity;

import com.example.codoceanb.profile.dto.TestCaseDTO;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "test_cases")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestCase implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "output_data")
    private String outputData;

    @Column(name = "is_public")
    private boolean isPublic = false;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL)
    private List<Parameter> parameters;

    public TestCaseDTO toDTO() {
        return TestCaseDTO.builder()
                .isPublic(this.isPublic)
                .parameterDTOs(this.parameters.stream()
                        .map(Parameter::toDTO)
                        .toList())
                .output(this.outputData)
                .build();
    }
}
