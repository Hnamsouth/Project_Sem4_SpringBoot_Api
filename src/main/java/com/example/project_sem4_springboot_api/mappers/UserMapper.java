package com.example.project_sem4_springboot_api.mappers;


import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import org.mapstruct.Mapper;

/**
 * The interface User mapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * To entity user.
     *
     * @param request the request
     * @return the user
     */
    User toEntity(RegisterRequest request);

    /**
     * To response user response.
     *
     * @param entity the entity
     * @return the user response
     */
    RegisterRequest toResponse(User entity);
}