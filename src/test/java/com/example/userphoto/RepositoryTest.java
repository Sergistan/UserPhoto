package com.example.userphoto;

import com.example.userphoto.models.Role;
import com.example.userphoto.models.User;
import com.example.userphoto.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;


    @BeforeEach
        void setUp() {
        user = new User(10L,
                "Random",
                "12345",
                LocalDate.now(),
                "random@mail.com",
                "+79811234567",
                "".getBytes(),
                Role.ROLE_USER);
        userRepository.save(user);
    }

    @Test
    void findUserById() {
        assertEquals(user, userRepository.findUserById(10L));
    }

    @Test
    void findUserByName() {
        assertEquals(Optional.of(user), userRepository.findUserByName(user.getName()));
    }

    @AfterEach
    void deleteSetUp() {
        userRepository.delete(user);
    }

}
