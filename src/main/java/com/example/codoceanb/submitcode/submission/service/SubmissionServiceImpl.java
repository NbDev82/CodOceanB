package com.example.codoceanb.submitcode.submission.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UserNotFoundException;
import com.example.codoceanb.auth.mapper.UserMapper;
import com.example.codoceanb.auth.repository.UserRepos;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.submitcode.DTO.ResultDTO;
import com.example.codoceanb.submitcode.DTO.SubmissionDTO;
import com.example.codoceanb.submitcode.ECompilerConstants;
import com.example.codoceanb.submitcode.exception.UnsupportedLanguageException;
import com.example.codoceanb.submitcode.parameter.service.ParameterService;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.service.ProblemService;
import com.example.codoceanb.submitcode.strategy.CompilerProcessor;
import com.example.codoceanb.submitcode.strategy.CompilerResult;
import com.example.codoceanb.submitcode.strategy.CompilerStrategy;
import com.example.codoceanb.submitcode.strategy.JavaCompiler;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import com.example.codoceanb.submitcode.submission.mapper.SubmissionMapper;
import com.example.codoceanb.submitcode.submission.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private CompilerStrategy compilerStrategy;

    private final SubmissionRepository submissionRepos;
    private final UserRepos userRepos;
    private final UserService userService;
    private final ParameterService parameterService;
    private final ProblemService problemService;
    private final UserMapper userMapper;
    private final SubmissionMapper submissionMapper;

    @Autowired
    public SubmissionServiceImpl(SubmissionRepository submissionRepos,
                                 UserRepos userRepos,
                                 UserService userService,
                                 ParameterService parameterService,
                                 ProblemService problemService,
                                 UserMapper userMapper,
                                 SubmissionMapper submissionMapper) {
        this.submissionRepos = submissionRepos;
        this.userRepos = userRepos;
        this.userService = userService;
        this.parameterService = parameterService;
        this.problemService = problemService;
        this.userMapper = userMapper;
        this.submissionMapper = submissionMapper;
    }

    @Override
    public String getInputCode(Problem problem, Submission.ELanguage eLanguage) {
        compilerStrategy = determineCompilerStrategy(eLanguage);
        return compilerStrategy.createInputCode(problem, "", problem.getTestCases().get(0));
    }

    @Override
    public ResultDTO compile(String authHeader, String code, Submission.ELanguage eLanguage) {
        compilerStrategy = determineCompilerStrategy(eLanguage);

        String fileName = "Solution.java";
        prepareFile(fileName, code);

        CompilerResult compilerResult = compilerStrategy.compile(code, fileName);

        return createResultDTO(compilerResult);
    }

    private ResultDTO createResultDTO(CompilerResult compilerResult) {
        return ResultDTO.builder()
                .isAccepted(compilerResult.getCompilerConstants().equals(ECompilerConstants.SUCCESS))
                .message(compilerResult.getError())
                .status(compilerResult.getCompilerConstants().equals(ECompilerConstants.SUCCESS)
                        ? Submission.EStatus.ACCEPTED
                        : Submission.EStatus.COMPILE_ERROR)
                .build();
    }

    @Override
    public ResultDTO runCode(String authHeader, String code, Problem problem, Submission.ELanguage eLanguage) {
        compilerStrategy = determineCompilerStrategy(eLanguage);
        User user = userService.getUserDetailsFromToken(authHeader);

        String fileName = "Solution.java";
        prepareFile(fileName, code);
        CompilerResult compilerResult = compilerStrategy.compile(code, fileName);

        if (compilerResult.getCompilerConstants().equals(ECompilerConstants.SUCCESS)) {
            CompilerProcessor compilerProcessor = new CompilerProcessor(compilerStrategy);
            ResultDTO resultDTO = compilerProcessor.run(code, problem);

            handleSuccessfulExecution(user, resultDTO, eLanguage, code, problem);

            return resultDTO;
        } else {
            handleCompilationError(user, code, problem);
            return createCompilationErrorResultDTO(compilerResult.getError());
        }
    }

    private ResultDTO createCompilationErrorResultDTO(String error) {
        return ResultDTO.builder()
                .status(Submission.EStatus.COMPILE_ERROR)
                .message(error)
                .isAccepted(false)
                .build();
    }

    private Submission createSubmission(User user, String code, Problem problem, Submission.ELanguage eLanguage, Submission.EStatus status) {
        return Submission.builder()
                .language(eLanguage)
                .codeSubmitted(code)
                .createdAt(LocalDateTime.now())
                .status(status)
                .user(user)
                .problem(problem)
                .build();
    }

    private void handleCompilationError(User user, String code, Problem problem) {
        Submission submission = createSubmission(user, code, problem, Submission.ELanguage.JAVA, Submission.EStatus.COMPILE_ERROR);
        addSubmission(user, submission);
    }

    private void handleSuccessfulExecution(User user, ResultDTO resultDTO, Submission.ELanguage eLanguage, String code, Problem problem) {
        if (!resultDTO.getStatus().equals(Submission.EStatus.COMPILE_ERROR)) {
            Submission submission = createSubmission(user, code, problem, eLanguage, resultDTO.getStatus());
            submission.setMemory(resultDTO.getMemory());
            submission.setRuntime(resultDTO.getRuntime());

            addSubmission(user, submission);
        }
    }

    private void prepareFile(String fileName, String code) {
        compilerStrategy.deleteFileCompiled();
        compilerStrategy.writeFile(fileName, code);
    }

    private CompilerStrategy determineCompilerStrategy(Submission.ELanguage eLanguage) {
        return switch (eLanguage) {
            case JAVA -> new JavaCompiler(parameterService);
            case PYTHON, CSHARP ->
                    throw new UnsupportedLanguageException("Language " + eLanguage.name().toLowerCase() + " is not supported yet!");
        };
    }

    @Override
    public List<SubmissionDTO> getByUserIdAndProblemId(String authHeader, UUID problemId) {
        User user = userService.getUserDetailsFromToken(authHeader);
        Problem problem = problemService.getEntityByProblemId(problemId);
        List<Submission> submissions = submissionRepos.findByUserAndProblem(user, problem);
        submissions.sort((submissionA, submissionB) -> submissionB.getCreatedAt().compareTo(submissionA.getCreatedAt()));
        return submissionMapper.toDTOs(submissions);
    }

    @Override
    public <T> List<T> getByUserId(UUID userId, Class<T> returnType) {
        User user = userMapper.toEntity(userService.getUserById(userId));

        List<Submission> submissions = submissionRepos.findByUser(user);

        return returnType.equals(Submission.class)
                ? (List<T>) submissions
                : submissions.stream()
                .map(submissionMapper::toDTO)
                .map(returnType::cast)
                .collect(Collectors.toList());
    }

    public void addSubmission(User user, Submission submission) {
        List<Submission> submissions = user.getSubmissions();
        if (submissions == null) {
            submissions = new ArrayList<>();
        }
        submissions.add(submission);
        user.setSubmissions(submissions);
        userRepos.save(user);
    }
}
