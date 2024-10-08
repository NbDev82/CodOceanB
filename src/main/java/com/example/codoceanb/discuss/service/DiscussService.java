package com.example.codoceanb.discuss.service;

import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.discuss.request.AddDiscussRequest;
import com.example.codoceanb.discuss.request.UpdateDiscussRequest;

import java.util.List;
import java.util.UUID;

public interface DiscussService {
    List<DiscussDTO> getAllUploadedDiscussesByUser(String token);

    List<DiscussDTO> getDiscusses(int pageNumber, int limit, String searchTerm, String category);

    DiscussDTO addDiscuss(AddDiscussRequest request, String authHeader);

    DiscussDTO updateDiscuss(UUID id, UpdateDiscussRequest request);

    void deleteDiscuss(UUID id);
}
