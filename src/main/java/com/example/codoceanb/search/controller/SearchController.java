package com.example.codoceanb.search.controller;

import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.search.dto.ProblemDTO;
import com.example.codoceanb.search.dto.SearchResultDTO;
import com.example.codoceanb.search.requestmodel.SearchRequest;
import com.example.codoceanb.search.service.SearchService;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private static final Logger log = LogManager.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @GetMapping("/problems")
    public ResponseEntity<SearchResultDTO> getProblems(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) SearchRequest.EStatus status,
            @RequestParam(required = false) Problem.EDifficultyLevel difficulty,
            @RequestParam(required = false) Problem.ETopic topic,
            @RequestParam(required = false) String searchTerm,
            @RequestHeader(value = "Authorization") String token) {
        
        if (!jwtTokenUtils.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        String email = jwtTokenUtils.extractEmailFromBearerToken(token);
        SearchRequest request = createSearchRequest(pageNumber, limit, status, difficulty, topic, searchTerm);
        SearchResultDTO searchResultDTO = searchService.getProblems(request, email);
        
        return ResponseEntity.ok(searchResultDTO);
    }
    

    
    private SearchRequest createSearchRequest(int pageNumber, int limit, SearchRequest.EStatus status,
                                              Problem.EDifficultyLevel difficulty, Problem.ETopic topic, String searchTerm) {
        return new SearchRequest(pageNumber, limit, status, difficulty, topic, searchTerm);
    }

    @GetMapping("/problems/{id}")
    public ResponseEntity<ProblemDTO> getProblemDetail(@PathVariable Long id, @RequestHeader(value = "Authorization") String token) {
        if (!jwtTokenUtils.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        String email = jwtTokenUtils.extractEmailFromBearerToken(token);
        ProblemDTO problemDetail = searchService.getProblemDetail(id, email);
        
        return ResponseEntity.ok(problemDetail);
    }
}