package com.example.userphoto;

import com.example.userphoto.models.Role;
import com.example.userphoto.models.User;
import com.example.userphoto.repositories.UserRepository;
import com.example.userphoto.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private final User user = new User(10L,
            "Random",
            "12345",
            LocalDate.now(),
            "random@mail.com",
            "+79811234567",
            "".getBytes(),
            Role.ROLE_USER);;


    @Test
    void findAllUsers() {
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(userService.allUsers(), List.of(user));
    }


}
