package com.example.userphoto;

import com.example.userphoto.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.example.userphoto.TestData.USER_WITH_ROLE_ADMIN;
import static com.example.userphoto.TestData.USER_WITH_ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(USER_WITH_ROLE_ADMIN);
    }

    @Test
    void findUserById() {
        assertEquals(USER_WITH_ROLE_ADMIN, userRepository.findUserById(1L));
    }

    @Test
    void findUserByName() {
        assertEquals(Optional.of(USER_WITH_ROLE_ADMIN), userRepository.findUserByName(USER_WITH_ROLE_ADMIN.getName()));
    }

    @Test
    void notFindUserById() {
        assertNull(userRepository.findUserById(0L));
    }

    @Test
    void notFindUserByName() {
        assertTrue(userRepository.findUserByName(USER_WITH_ROLE_USER.getName()).isEmpty());
    }

    @AfterEach
    void deleteSetUp() {
        userRepository.delete(USER_WITH_ROLE_ADMIN);
    }

}
