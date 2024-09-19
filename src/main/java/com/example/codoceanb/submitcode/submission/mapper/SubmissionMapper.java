package com.example.codoceanb.submitcode.submission.mapper;

import com.example.codoceanb.submitcode.DTO.SubmissionDTO;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
    SubmissionMapper INSTANCE = Mappers.getMapper(SubmissionMapper.class);

    @Mapping(target = "createdAt",
            expression = "java(formatDateString(submission.getCreatedAt()))")
    SubmissionDTO toDTO(Submission submission);

    List<SubmissionDTO> toDTOs(List<Submission> submissions);

    @Mapping(target = "problem", ignore = true)
    @Mapping(target = "user", ignore = true)
    Submission toEntity(SubmissionDTO submissionDTO);

    List<Submission> toEntities(List<SubmissionDTO> submissionDTOs);

    default String formatDateString(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("LocalDateTime cannot be null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return date.format(formatter);
    }

    default String formatDoubleParam(double param) {
        DecimalFormat df = new DecimalFormat("#.######");
        return df.format(param);
    }
}
