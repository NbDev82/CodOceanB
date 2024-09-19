package com.example.codoceanb.submitcode.problem.mapper;

import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProblemMapper {
    ProblemMapper INSTANCE = Mappers.getMapper(ProblemMapper.class);

    @Mapping(target = "submissionCount",
            expression = "java(problem.getSubmissions() != null ? problem.getSubmissions().size() : 0)")
    @Mapping(target = "discussCount",
            expression = "java(countComment(problem))")
    @Mapping(target = "acceptedCount",
            expression = "java(countAccepted(problem))")
    @Mapping(source = "id", target = "id")
    ProblemDTO toDTO(Problem problem);

    List<ProblemDTO> toDTOs(List<Problem> problems);

    @Mapping(target = "librariesSupports", ignore = true)
    @Mapping(target = "testCases", ignore = true)
    @Mapping(target = "functionName", ignore = true)
    @Mapping(target = "outputDataType", ignore = true)
    Problem toEntity(ProblemDTO problemDTO);

    List<Problem> toEntities(List<ProblemDTO> problemDTOs);

    default int countAccepted(Problem problem) {
        return problem.getSubmissions() != null ? (int) problem.getSubmissions().stream().filter(s -> s.getStatus().equals(Submission.EStatus.ACCEPTED)).count() : 0;
    }

    default int countComment(Problem problem) {
        return problem.getComments() != null ? (int) problem.getComments().stream().filter(c -> !c.isDeleted()).count() : 0;
    }
}
