package com.erick.userserviceapi.mapper;

import com.erick.userserviceapi.entity.User;
import models.responses.UserResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@Mapper(
        componentModel = "spring",
//      nullValuePropertyMappingStrategy = IGNORE, (commented parameter because I still don't understand how it works)
        nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {

    UserResponse toUserResponse(final User user);

}

