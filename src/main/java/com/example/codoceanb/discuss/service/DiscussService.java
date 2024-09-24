package com.example.codoceanb.discuss.service;

import com.example.codoceanb.discuss.dto.DiscussDTO;

import java.util.List;

public interface DiscussService {
    List<DiscussDTO> getAllUploadedDiscussesByUser(String token);
}
