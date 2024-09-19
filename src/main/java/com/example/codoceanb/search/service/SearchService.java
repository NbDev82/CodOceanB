package com.example.codoceanb.search.service;

import com.example.codoceanb.search.dto.ProblemDTO;
import com.example.codoceanb.search.dto.SearchResultDTO;
import com.example.codoceanb.search.requestmodel.SearchRequest;

public interface SearchService {
    SearchResultDTO getProblems(SearchRequest request, String email);

    ProblemDTO getProblemDetail(Long id, String email);
}
