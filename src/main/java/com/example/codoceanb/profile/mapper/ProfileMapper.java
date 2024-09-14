package com.example.codoceanb.profile.mapper;

import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.login.entity.User;
import com.example.codoceanb.login.mapper.UserMapper;
import com.example.codoceanb.profile.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "urlImage", source = "urlImage")
    User toEntity(ProfileDTO userDTO);
}
