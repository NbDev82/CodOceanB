package com.example.codoceanb.auth.mapper;

import com.example.codoceanb.auth.dto.UserDTO;
import com.example.codoceanb.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "urlImage", source = "urlImage")
    User toEntity(UserDTO userDTO);

    List<User> toEntities(List<UserDTO> userDTOs);
}
