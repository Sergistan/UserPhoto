package com.example.userphoto.Util;

import com.example.userphoto.models.User;
import com.example.userphoto.models.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserToUserDTO {

    UserDTO toUserDTO (User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "photo", ignore = true)
    User toUser (UserDTO userDTO);

}
