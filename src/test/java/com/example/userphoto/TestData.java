package com.example.userphoto;

import com.example.userphoto.models.Role;
import com.example.userphoto.models.User;
import com.example.userphoto.models.UserDTO;

import java.time.LocalDate;

public class TestData {

    public static final User USER_WITH_ROLE_ADMIN = new User(1L,
            "RoleAdmin",
            "12345",
            LocalDate.now(),
            "RoleAdmin@mail.com",
            "+79811234567",
            "".getBytes(),
            Role.ROLE_ADMIN);

    public static final UserDTO USER_WITH_ROLE_ADMIN_DTO = new UserDTO(
            "RoleAdmin",
            "12345",
            LocalDate.now(),
            "RoleAdmin@mail.com",
            "+79811234567");

    public static final User USER_WITH_ROLE_USER = new User(2L,
            "RoleUser",
            "12345",
            LocalDate.now(),
            "RoleUser@mail.com",
            "+79811234567",
            "".getBytes(),
            Role.ROLE_USER);

}
