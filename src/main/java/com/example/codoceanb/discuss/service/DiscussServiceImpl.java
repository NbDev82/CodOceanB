package com.example.codoceanb.discuss.service;

import com.example.codoceanb.discuss.entity.Discuss;
import com.example.codoceanb.discuss.repository.DiscussRepository;
import com.example.codoceanb.infras.security.JwtTokenUtils;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.search.service.SearchServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscussServiceImpl implements DiscussService{
    private static final Logger log = LogManager.getLogger(SearchServiceImpl.class);

    @Autowired
    private DiscussRepository discussRepository;
    
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public List<DiscussDTO> getAllUploadedDiscussesByUser(String token) {
        try {
            String email = jwtTokenUtils.extractEmail(token);
            List<Discuss> discusses = discussRepository.findByOwnerEmail(email);
            return discusses.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (io.jsonwebtoken.io.DecodingException e) {
            log.error("Error decoding token: ", e);
            throw new IllegalArgumentException("Invalid token");
        } catch (Exception e) {
            log.error("Error retrieving discussions by owner: ", e);
            throw new RuntimeException("Unable to retrieve discussions");
        }
    }

    private DiscussDTO convertToDTO(Discuss discuss) {
        return DiscussDTO.builder()
                .id(discuss.getId())
                .description(discuss.getDescription())
                .content(discuss.getContent())
                .createdAt(discuss.getCreatedAt())
                .updatedAt(discuss.getUpdatedAt())
                .endAt(discuss.getUpdatedAt())
                .build();
    }
}
