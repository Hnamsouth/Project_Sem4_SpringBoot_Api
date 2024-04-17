package com.example.project_sem4_springboot_api.mappers;


import com.example.project_sem4_springboot_api.dto.UserDetailDto;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * The interface User mapper.
 */
@Mapper(componentModel = "spring")
@Service
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    /**
     * To response user response.
     *
     * @param entity the entity
     * @return the user response
     */
    RegisterRequest registerRequestToUser(User entity);

    @Mapping(source = "first_name", target = "firstname")
    @Mapping(source = "last_name", target = "lastname")
    UserDetail registerRequestToUserDetail(RegisterRequest entity);

    UserDetail dtoToUserDetail(UserDetailDto entity);


}