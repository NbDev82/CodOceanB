package com.example.codoceanb.search.service;

import com.example.codoceanb.search.dto.ProblemDTO;
import com.example.codoceanb.search.dto.SearchResultDTO;
import com.example.codoceanb.search.exception.ProblemNotFoundException;
import com.example.codoceanb.search.mapper.SearchProblemMapper;
import com.example.codoceanb.search.requestmodel.SearchRequest;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger log = LogManager.getLogger(SearchServiceImpl.class);

    private final SearchProblemMapper mapper;
    private final ProblemRepository problemRepos;

    @Autowired
    public SearchServiceImpl(SearchProblemMapper mapper, ProblemRepository problemRepos) {
        this.mapper = mapper;
        this.problemRepos = problemRepos;
    }

    @Override
    public SearchResultDTO getProblems(SearchRequest request, String email) {
        try {
            String ALL_TERM = "all";

            Problem.EDifficultyLevel difficulty = request.getDifficulty();
            Problem.ETopic topic = request.getTopic();
            SearchRequest.EStatus status = request.getStatus();
            String searchTerm = request.getSearchTerm();

            Pageable sortedById = PageRequest.of(request.getPageNumber(), request.getLimit(), Sort.by("id"));
            Page<Problem> problemPage = problemRepos.findByCriteria(difficulty, sortedById);

            List<ProblemDTO> problemDTOs = new ArrayList<>(problemPage.stream()
                    .filter(problem -> topic == null || problem.getTopics().contains(topic))
                    .filter(problem -> searchTerm.isEmpty() || problem.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .map(problem -> enrichProblemDTO(problem, email))
                    .filter(problemDTO -> status == null || problemDTO.getStatus().equals(status))
                    .toList());

            return new SearchResultDTO(
                    problemPage.getTotalPages(),
                    problemPage.getTotalElements(),
                    problemDTOs);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProblemNotFoundException(e.getMessage());
        }
    }

    private <T extends Enum<T>> T parseEnum(String value, String allTerm, Class<T> enumClass) {
        return value.equalsIgnoreCase(allTerm) ? null : Enum.valueOf(enumClass, value.toUpperCase());
    }

    private ProblemDTO enrichProblemDTO(Problem problem, String email) {
        ProblemDTO problemDTO = mapper.toDTO(problem, email);

        long attemptedCount = problem.getSubmissions()
                .stream()
                .filter(submission -> submission.getUser().getEmail().equals(email))
                .count();

        long acceptedCount = problem.getSubmissions()
                .stream()
                .filter(submission -> submission.getUser().getEmail().equals(email)
                        && submission.getStatus().equals(Submission.EStatus.ACCEPTED))
                .count();

        problemDTO.setStatus(calculateUserStatus(attemptedCount, acceptedCount));
        problemDTO.setAcceptanceRate();

        return problemDTO;
    }

    private SearchRequest.EStatus calculateUserStatus(long attemptedCount, long acceptedCount) {
        if (attemptedCount <= 0) {
            return SearchRequest.EStatus.TODO;
        } else if (acceptedCount > 0) {
            return SearchRequest.EStatus.SOLVED;
        } else {
            return SearchRequest.EStatus.ATTEMPTED;
        }
    }

    @Override
    public ProblemDTO getProblemDetail(Long id, String email) {
        Problem problem = problemRepos.findById(id)
                .orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
        return mapper.toDTO(problem, null);
    }
}