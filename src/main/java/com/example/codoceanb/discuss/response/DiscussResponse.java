package com.example.codoceanb.discuss.response;

import com.example.codoceanb.discuss.dto.DiscussDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscussResponse {
    private int totalPage;
    private long totalElement;
    List<DiscussDTO> discussDTOs;
}
