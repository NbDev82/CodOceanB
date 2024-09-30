package com.example.codoceanb.search.service;

import com.example.codoceanb.search.dto.ProblemDTO;
import com.example.codoceanb.search.dto.SearchResultDTO;
import com.example.codoceanb.search.requestmodel.SearchRequest;
import java.util.UUID;

public interface SearchService {
    SearchResultDTO getProblems(SearchRequest request, String authHeader);

    ProblemDTO getProblemDetail(UUID id);
}
