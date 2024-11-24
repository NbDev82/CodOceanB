package com.example.codoceanb.submitcode.problem.service;

import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.statistic.dto.TrendingProblemDTO;
import com.example.codoceanb.submitcode.DTO.*;
import com.example.codoceanb.submitcode.exception.ProblemNotFoundException;
import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import com.example.codoceanb.submitcode.library.repository.LibraryRepository;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.parameter.repository.ParameterRepository;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.entity.ProblemApproach;
import com.example.codoceanb.submitcode.problem.entity.ProblemHint;
import com.example.codoceanb.submitcode.problem.mapper.ProblemMapper;
import com.example.codoceanb.submitcode.problem.repository.ProblemApproachRepository;
import com.example.codoceanb.submitcode.problem.repository.ProblemHintRepository;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
import com.example.codoceanb.submitcode.request.AddProblemRequest;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import com.example.codoceanb.submitcode.testcase.repository.TestCaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProblemServiceImpl implements ProblemService{
    private static final Logger log = LogManager.getLogger(ProblemServiceImpl.class);

    private final ProblemRepository problemRepository;
    private final ProblemApproachRepository problemApproachRepository;
    private final ProblemHintRepository problemHintRepository;
    private final TestCaseRepository testCaseRepository;
    private final ParameterRepository parameterRepository;
    private final LibraryRepository libraryRepository;

    private final ProblemMapper mapper;

    private final UserService userService;

    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public ProblemServiceImpl(ProblemRepository problemRepository,
                              ProblemApproachRepository problemApproach, ProblemHintRepository problemHintRepository, TestCaseRepository testCaseRepository, ParameterRepository parameterRepository, LibraryRepository libraryRepository, ProblemMapper mapper, UserService userService, JwtTokenUtils jwtTokenUtils) {
        this.problemRepository = problemRepository;
        this.problemApproachRepository = problemApproach;
        this.problemHintRepository = problemHintRepository;
        this.testCaseRepository = testCaseRepository;
        this.parameterRepository = parameterRepository;
        this.libraryRepository = libraryRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public <T> T findById(UUID problemId, Class<T> returnType) {
        Problem problem = getEntityByProblemId(problemId);
        log.info("get problemDTO from ProblemServiceImpl");

        return returnType.equals(Problem.class)
                ? returnType.cast(problem)
                : returnType.cast(problem.toDTO());
    }

    @Override
    public List<Problem> getAll() {
        return problemRepository.getAll();
    }

    @Override
    public List<ProblemDTO> getAllDTOs() {
        return getAll().stream().map(Problem::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProblemDTO> getAllUploadedProblemsByUser(String token) {
        String email = jwtTokenUtils.extractEmailFromBearerToken(token);
        return problemRepository.getProblemsByOwner(email).stream().map(Problem::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProblemDTO> getAllSolvedProblemsByUser(String token) {
        String email = jwtTokenUtils.extractEmailFromBearerToken(token);
        List<Problem> solvedProblems = problemRepository.findSolvedProblemsByUser(email);
        return solvedProblems.stream().map(Problem::toDTO).collect(Collectors.toList());
    }

    @Override
    public Problem getEntityByProblemId(UUID problemId) {
        log.info("get problem from ProblemServiceImpl");
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Requested problem not found"));
    }

    @Override
    public Boolean add(AddProblemRequest request, String authHeader) {
        try{
            Problem problem = createProblemFromRequest(request.getProblem(), authHeader);
            problem = problemRepository.save(problem);
            createAndSaveHintFromRequest(request, problem);
            createAndSaveTestCaseFromRequest(request, problem);
            createAndSaveLibraryFromRequest(request,problem);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    private void createAndSaveHintFromRequest(AddProblemRequest request, Problem problem) {
        ProblemHintDTO problemHintDTO = request.getProblem().getProblemHintDTO();
        ProblemHint hint = ProblemHint.builder()
                .isLocked(problemHintDTO.isLocked())
                .overView(problemHintDTO.getOverView())
                .pseudoCode(problemHintDTO.getPseudoCode())
                .build();
        ProblemHint savedHint = problemHintRepository.save(hint);

        problem.setProblemHint(hint);
        problemRepository.save(problem);

        createAndSaveProblemApproachFromHint(problemHintDTO, savedHint);
    }

    private void createAndSaveProblemApproachFromHint(ProblemHintDTO problemHintDTO, ProblemHint hint) {
        for (ProblemApproachDTO approachDTO: problemHintDTO.getApproachDTOs()) {
            ProblemApproach problemApproach = ProblemApproach.builder()
                    .intuition(approachDTO.getIntuition())
                    .algorithm(approachDTO.getAlgorithm())
                    .implementation(approachDTO.getImplementation())
                    .language(approachDTO.getLanguage())
                    .problemHint(hint)
                    .build();
            problemApproachRepository.save(problemApproach);
            
            List<ProblemApproach> problemApproaches = hint.getApproaches();
            if (problemApproaches == null) {
                problemApproaches = new ArrayList<>();
            }
            problemApproaches.add(problemApproach);
            hint.setApproaches(problemApproaches);
            problemHintRepository.save(hint);
        }
    }

    @Override
    public Boolean delete(UUID problemId) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(()->new ProblemNotFoundException("Can't find problem with id "+problemId));
        problem.setDeleted(true);
        problemRepository.save(problem);
        return true;
    }

    @Override
    public Problem getRandomProblem() {
        List<Problem> problems = problemRepository.findAllProblemAvailable();
        long count = problems.size();
        int randomIndex = new Random().nextInt((int) count);
        return problems.get(randomIndex);
    }

    @Override
    public List<TrendingProblemDTO> getTopProblemsByTopic(String topic, int limit) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.valueOf(topic));
        List<Problem> problems = problemRepository.findTopByTopicsOrderBySubmissionsDesc(topics, PageRequest.of(0, limit));
        return problems.stream().map(Problem::toTrendingDTO).collect(Collectors.toList());
    }

    @Override
    public List<TrendingProblemDTO> getTopProblems(int limit) {
        List<Problem> topProblems = problemRepository.findTopByOrderBySubmissionsDesc(PageRequest.of(0, limit));
        return topProblems.stream().map(Problem::toTrendingDTO).collect(Collectors.toList());
    }

    @Override
    public String getCorrectAnswer(UUID problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
        return problem.getCorrectAnswer();
    }


    private void createAndSaveLibraryFromRequest(AddProblemRequest request, Problem problem) {
        List<String> libraries = request.getLibraries();

        LibrariesSupport librariesSupport;

        for (String library : libraries) {
            librariesSupport = LibrariesSupport.builder()
                    .name(library)
                    .problem(problem)
                    .build();
            libraryRepository.save(librariesSupport);
        }
    }

    private void createAndSaveTestCaseFromRequest(AddProblemRequest request, Problem problem) {
        List<AddTestCaseRequestDTO> testcaseDTOs = request.getTestcases();

        TestCase testCase;
        TestCase.TestCaseBuilder testCaseBuilder = TestCase.builder();
        for (AddTestCaseRequestDTO dto : testcaseDTOs) {
            testCase = testCaseBuilder
                    .isPublic(dto.isPublic())
                    .outputData(dto.getOutput())
                    .problem(problem)
                    .build();
            createAndSaveParameterFromRequest(testCase, dto.getInput());
            testCaseRepository.save(testCase);
        }
    }

    private Problem createProblemFromRequest(AddProblemRequestDTO problemDTO, String authHeader) {
        Problem.EDifficulty difficulty = Problem.EDifficulty.valueOf(problemDTO.getDifficulty());

        User user = userService.getUserDetailsFromToken(authHeader);
        List<Problem.ETopic> topics = new ArrayList<>();

        return Problem.builder()
                .title(problemDTO.getTitle())
                .description(problemDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .functionName(problemDTO.getFunctionName())
                .correctAnswer(problemDTO.getCorrectAnswer())
                .outputDataType(problemDTO.getOutputDataType())
                .difficulty(difficulty)
                .isDeleted(problemDTO.isDeleted())
                .owner(user)
                .topics(topics)
                .build();
    }

    private void createAndSaveParameterFromRequest(TestCase testCase, List<InputDTO> inputDTOs) {
        for (InputDTO input : inputDTOs) {
            Parameter parameter = Parameter.builder()
                    .inputDataType(input.getDatatype())
                    .name(input.getParamName())
                    .inputData(input.getValue())
                    .index(input.getIndex())
                    .testCase(testCase)
                    .build();
            parameterRepository.save(parameter);
        }
    }
}
