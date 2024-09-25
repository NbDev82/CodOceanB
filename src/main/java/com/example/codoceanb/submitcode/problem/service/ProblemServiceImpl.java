package com.example.codoceanb.submitcode.problem.service;

import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.submitcode.DTO.AddProblemRequestDTO;
import com.example.codoceanb.submitcode.DTO.AddTestCaseRequestDTO;
import com.example.codoceanb.submitcode.DTO.InputDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.exception.ProblemNotFoundException;
import com.example.codoceanb.submitcode.library.entity.LibrariesSupport;
import com.example.codoceanb.submitcode.library.repository.LibraryRepository;
import com.example.codoceanb.submitcode.parameter.entity.Parameter;
import com.example.codoceanb.submitcode.parameter.repository.ParameterRepository;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.mapper.ProblemMapper;
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

@Service
public class ProblemServiceImpl implements ProblemService{
    private static final Logger log = LogManager.getLogger(ProblemServiceImpl.class);

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final ParameterRepository parameterRepository;
    private final LibraryRepository libraryRepository;

    private final ProblemMapper mapper;

    private final UserService userService;

    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public ProblemServiceImpl(ProblemRepository problemRepository,
                              TestCaseRepository testCaseRepository, ParameterRepository parameterRepository, LibraryRepository libraryRepository, ProblemMapper mapper, UserService userService, JwtTokenUtils jwtTokenUtils) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
        this.parameterRepository = parameterRepository;
        this.libraryRepository = libraryRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public <T> T findById(Long problemId, Class<T> returnType) {
        Problem problem = getEntityByProblemId(problemId);
        log.info("get problemDTO from ProblemServiceImpl");

        return returnType.equals(Problem.class)
                ? returnType.cast(problem)
                : returnType.cast(mapper.toDTO(problem));
    }

    @Override
    public List<Problem> getAll() {
        return problemRepository.findAll();
    }

    @Override
    public List<ProblemDTO> getAllDTOs() {
        return mapper.toDTOs(getAll());
    }

    @Override
    public List<ProblemDTO> getAllUploadedProblemsByUser(String token) {
        String email = jwtTokenUtils.extractEmailFromBearerToken(token);
        return mapper.toDTOs(problemRepository.getProblemsByOwner(email));
    }

    @Override
    public List<ProblemDTO> getAllSolvedProblemsByUser(String token) {
        String email = jwtTokenUtils.extractEmailFromBearerToken(token);
        List<Problem> solvedProblems = problemRepository.findSolvedProblemsByUser(email);
        return mapper.toDTOs(solvedProblems);
    }

    @Override
    public Problem getEntityByProblemId(Long problemId) {
        log.info("get problem from ProblemServiceImpl");
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemNotFoundException("Requested problem not found"));
    }

    @Override
    public Boolean add(AddProblemRequest request) {
        try{
            Problem problem = createProblemFromRequest(request.getProblem());
            createAndSaveTestCaseFromRequest(request, problem);
            createAndSaveLibraryFromRequest(request,problem);
            return problem != null;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Long problemId) {
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
    public List<ProblemDTO> getTopProblemsByTopic(String topic, int limit) {
        List<Problem.ETopic> topics = new ArrayList<>();
        topics.add(Problem.ETopic.valueOf(topic));
        return mapper.toDTOs(problemRepository.findTopByTopicsOrderBySubmissionsDesc(topics, PageRequest.of(0, limit)));
    }

    @Override
    public List<ProblemDTO> getTopProblems(int limit) {
        List<Problem> topProblems = problemRepository.findTopByOrderBySubmissionsDesc(PageRequest.of(0, limit));
        return mapper.toDTOs(topProblems);
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
                    .outputData(dto.getOutput())
                    .problem(problem)
                    .build();
            createAndSaveParameterFromRequest(testCase, dto.getInput());
            testCaseRepository.save(testCase);
        }
    }

    private Problem createProblemFromRequest(AddProblemRequestDTO problemDTO) {
        Problem.EDifficultyLevel difficulty = Problem.EDifficultyLevel.valueOf(problemDTO.getDifficulty());

        User user = userService.getEntityUserById(problemDTO.getOwnerId());
        List<Problem.ETopic> topics = new ArrayList<>();

        return Problem.builder()
                .name(problemDTO.getTitle())
                .description(problemDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .functionName(problemDTO.getFunctionName())
                .outputDataType(problemDTO.getOutputDataType())
                .difficultyLevel(difficulty)
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
                    .testCase(testCase)
                    .build();
            parameterRepository.save(parameter);
        }
    }
}
