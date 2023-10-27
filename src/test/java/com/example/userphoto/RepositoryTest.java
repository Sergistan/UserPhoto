package com.example.userphoto;

import com.example.userphoto.models.Role;
import com.example.userphoto.models.User;
import com.example.userphoto.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void findUserById() {
        User admin = createAdmin();
        assertEquals(admin, userRepository.findUserById(1L));
    }

    @Test
    void findUserByName() {
        User admin = createAdmin();
        userRepository.save(admin);
        assertEquals(Optional.of(admin), userRepository.findUserByName(admin.getName()));
    }

    @Test
    void notFindUserById() {
        User user = createUser();
        assertNull(userRepository.findUserById(user.getId()));
    }

    @Test
    void notFindUserByName() {
        User user = createUser();
        assertTrue(userRepository.findUserByName(user.getName()).isEmpty());
    }

    @AfterEach
    void deleteSetUp() {
        User admin = createAdmin();
        userRepository.delete(admin);
    }


    private User createUser() {
        return new User(1000L,
                "RoleUser",
                "12345",
                LocalDate.now(),
                "RoleUser@mail.com",
                "+79811234567",
                "".getBytes(),
                Role.ROLE_USER);
    }

    private User createAdmin() {
        return new User(1L,
                "RoleAdmin",
                "12345",
                LocalDate.now(),
                "RoleAdmin@mail.com",
                "+79811234567",
                "".getBytes(),
                Role.ROLE_ADMIN);
    }

}
