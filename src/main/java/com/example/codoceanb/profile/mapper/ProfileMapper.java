package com.example.codoceanb.profile.mapper;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileDTO toDTO(User user);

    List<ProfileDTO> toDTOs(List<User> users);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "urlImage", source = "urlImage")
    @Mapping(target = "VIPExpDate", source = "VIPExpDate")
    User toEntity(ProfileDTO userDTO);
}
