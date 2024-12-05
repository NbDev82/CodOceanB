package com.example.codoceanb.submitcode.problem.service;

import com.example.codoceanb.profile.dto.TestCaseDTO;
import com.example.codoceanb.submitcode.DTO.ProblemApproachDTO;
import com.example.codoceanb.submitcode.DTO.ProblemHintDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.entity.ProblemApproach;
import com.example.codoceanb.submitcode.problem.entity.ProblemHint;
import com.example.codoceanb.submitcode.problem.repository.ProblemApproachRepository;
import com.example.codoceanb.submitcode.problem.repository.ProblemHintRepository;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
import com.example.codoceanb.submitcode.testcase.entity.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfileProblemServiceImpl implements ProfileProblemService{
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemHintRepository problemHintRepository;
    @Autowired
    private ProblemApproachRepository problemApproachRepository;
    

    @Override
    public void updateTitle(UUID id, String title) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setTitle(title);
        problemRepository.save(problem);
    }

    @Override
    public void updateDifficulty(UUID id, Problem.EDifficulty difficulty) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setDifficulty(difficulty);
        problemRepository.save(problem);
    }

    @Override
    public void updateDescription(UUID id, String description) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        problem.setDescription(description);
        problemRepository.save(problem);
    }

    @Override
    @Transactional
    public void updateProblemHint(UUID problemId, ProblemHintDTO problemHintDTO) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        
        if (problem.getProblemHint() != null) {
            ProblemHint oldHint = problem.getProblemHint();
            problemHintRepository.delete(oldHint);
            problemHintRepository.flush();
            
            if (problemHintRepository.existsById(oldHint.getId())) {
                throw new RuntimeException("Cannot delete old hint");
            }
        }

        ProblemHint newHint = ProblemHint.builder()
                .overView(problemHintDTO.getOverView())
                .pseudoCode(problemHintDTO.getPseudoCode())
                .problem(problem)
                .build();
        problem.setProblemHint(newHint);

        ProblemHint savedNewHint = problemHintRepository.save(newHint);

        saveApproach(problemHintDTO.getApproachDTOs(), savedNewHint);

        problemRepository.save(problem);
    }

    private void saveApproach(List<ProblemApproachDTO> approachDTOs, ProblemHint savedNewHint) {
        List<ProblemApproach> approaches = savedNewHint.getApproaches();
        for(ProblemApproachDTO problemApproachDTO : approachDTOs) {
            ProblemApproach problemApproach = ProblemApproach.builder()
                    .language(problemApproachDTO.getLanguage())
                    .algorithm(problemApproachDTO.getAlgorithm())
                    .intuition(problemApproachDTO.getIntuition())
                    .implementation(problemApproachDTO.getImplementation())
                    .problemHint(savedNewHint)
                    .build();
            problemApproach = problemApproachRepository.save(problemApproach);

            if(approaches == null || approaches.isEmpty()) {
                approaches = new ArrayList<>();
            }

            approaches.add(problemApproach);
            savedNewHint.setApproaches(approaches);
        }
    }

    @Override
    public List<TestCaseDTO> getTestCases(UUID problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        return problem.getTestCases().stream().map(tc -> tc.toDTO()).collect(Collectors.toList());
    }

//    @Override
//    @Transactional
//    public void updateTestCases(UUID problemId, List<TestCaseDTO> testCases) {
//        Problem problem = problemRepository.findById(problemId)
//                .orElseThrow(() -> new RuntimeException("Problem not found"));
//
//        // Xóa các test case cũ
//        if (!problem.getTestCases().isEmpty()) {
//            testCaseRepository.deleteAll(problem.getTestCases());
//            testCaseRepository.flush();
//        }
//
//        testCases.forEach(testCase -> {
//            testCase.setProblem(problem);
//            testCaseRepository.save(testCase);
//        });
//
//        problem.setTestCases(testCases);
//        problemRepository.save(problem);
//    }
}
