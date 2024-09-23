package com.example.codoceanb.search.controller;

import com.example.codoceanb.login.service.UserService;
import com.example.codoceanb.search.dto.ProblemDTO;
import com.example.codoceanb.search.dto.SearchResultDTO;
import com.example.codoceanb.search.requestmodel.SearchRequest;
import com.example.codoceanb.search.service.SearchService;
import com.example.codoceanb.infras.security.JwtTokenUtils;
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
    private UserService userService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @GetMapping("/problems")
    public ResponseEntity<SearchResultDTO> getProblems(@RequestBody SearchRequest request,
                                                       @RequestHeader(value = "Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        token = token.substring(7);
        String email = jwtTokenUtils.extractEmail(token);
        SearchResultDTO searchResultDTO = searchService.getProblems(request, email);
        return ResponseEntity.ok(searchResultDTO);
    }

    @GetMapping("/problems/{id}")
    public ResponseEntity<ProblemDTO> getProductDetail(@PathVariable Long id, @RequestHeader(value = "Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        token = token.substring(7);
        String email = jwtTokenUtils.extractEmail(token);
        ProblemDTO productDetail = searchService.getProblemDetail(id, email);
        return ResponseEntity.ok(productDetail);
    }
}