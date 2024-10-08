package com.example.codoceanb.search.mapper;

import com.example.codoceanb.search.dto.ProblemDTO;
import com.example.codoceanb.search.requestmodel.SearchRequest;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchProblemMapper {
    SearchProblemMapper INSTANCE = Mappers.getMapper(SearchProblemMapper.class);
    @Mapping(target = "submissionCount",
            expression = "java(problem.getSubmissions() != null ? problem.getSubmissions().size() : 0)")
    @Mapping(target = "acceptedCount",
            expression = "java(countAccepted(problem))")
    @Mapping(target = "title",
            expression = "java(problem.getTitle())")
    @Mapping(target = "difficulty",
            expression = "java(problem.getDifficulty())")
    @Mapping(target = "status",
            expression = "java(getStatus(problem,email))")
    @Mapping(target = "id",
            expression = "java(problem.getId())")
    ProblemDTO toDTO(Problem problem, String email);

    List<ProblemDTO> toDTOs(List<Problem> problems);

    @Mapping(target = "librariesSupports", ignore = true)
    @Mapping(target = "testCases", ignore = true)
    @Mapping(target = "functionName", ignore = true)
    @Mapping(target = "outputDataType", ignore = true)
    @Mapping(target = "description", ignore = true)
    Problem toEntity(ProblemDTO problemDTO);

    List<Problem> toEntities(List<ProblemDTO> problemDTOs);

    default int countAccepted(Problem problem) {
        return problem.getSubmissions() != null ? (int) problem.getSubmissions().stream().filter(s -> s.getStatus().equals(Submission.EStatus.ACCEPTED)).count() : 0;
    }
    default SearchRequest.EStatus getStatus(Problem problem, String email) {

        long attemptedCount = problem.getSubmissions()
                .stream()
                .filter(submission -> submission.getUser().getEmail().equals(email))
                .count();

        long acceptedCount = problem.getSubmissions()
                .stream()
                .filter(submission -> submission.getUser().getEmail().equals(email)
                        && submission.getStatus().equals(Submission.EStatus.ACCEPTED))
                .count();

        if (attemptedCount <= 0) {
            return SearchRequest.EStatus.TODO;
        } else if (acceptedCount > 0) {
            return SearchRequest.EStatus.SOLVED;
        } else {
            return SearchRequest.EStatus.ATTEMPTED;
        }
    }
}
