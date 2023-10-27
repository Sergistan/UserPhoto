package com.example.userphoto;

import com.example.userphoto.Util.UserToUserDTO;
import com.example.userphoto.exceptions.ErrorInputFile;
import com.example.userphoto.exceptions.ErrorUserDoesNotExist;
import com.example.userphoto.exceptions.ErrorUserForbidden;
import com.example.userphoto.models.*;
import com.example.userphoto.repositories.UserRepository;
import com.example.userphoto.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserToUserDTO mapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void findAllUsers() {
        User admin = createAdmin();
        User user = createUser();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(admin, user));
        Assertions.assertEquals(userService.allUsers(), List.of(admin, user));
    }

    @Test
    void notFindAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(null);
        Assertions.assertNull(userService.allUsers());
    }

    @Test
    void findUserById() {
        User admin = createAdmin();
        setUpAuthentication(admin);
        Mockito.when(userRepository.findUserById(admin.getId())).thenReturn(admin);

        UserDTO dtoAdmin = createDtoAdmin();
        Mockito.when(mapper.toUserDTO(admin)).thenReturn(dtoAdmin);

        Assertions.assertEquals(userService.findUserById(admin.getId()), dtoAdmin);
    }

    @Test
    void notFindUserById() {
        Mockito.when(userRepository.findUserById(0L)).thenReturn(null);
        Assertions.assertThrows(ErrorUserDoesNotExist.class,()-> userService.findUserById(0L));
    }

    @Test
    void notFindUserByIdFailAuth() {
        User admin = createAdmin();
        setUpAuthenticationFail(admin);
        Mockito.when(userRepository.findUserById(admin.getId())).thenReturn(admin);
        Assertions.assertThrows(ErrorUserForbidden.class,()-> userService.findUserById(admin.getId()));
    }


    @Test
    void postUser() {
        User admin = createAdmin();
        UserDTO dtoAdmin = createDtoAdmin();

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

        Mockito.when(mapper.toUser(dtoAdmin)).thenReturn(admin);
        Mockito.when(multipartFile.getContentType()).thenReturn("image/png");
        Mockito.when(passwordEncoder.encode(dtoAdmin.password())).thenReturn(String.valueOf(dtoAdmin));
        Mockito.when(userRepository.save(admin)).thenReturn(admin);

        Assertions.assertEquals(userService.postUser(dtoAdmin, multipartFile), admin);
    }

    @Test
    void postUserErrorValidFile() {
        User admin = createAdmin();
        UserDTO dtoAdmin = createDtoAdmin();

        Path path = Paths.get("src/main/resources/static/TestNotValidFile.txt");
        String name = "file.txt";
        String originalFileName = "file.txt";
        String contentType = "text/plain";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException ignored) {
        }

        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);

        Mockito.when(mapper.toUser(dtoAdmin)).thenReturn(admin);

        Assertions.assertThrows(ErrorInputFile.class, ()-> userService.postUser(dtoAdmin, result));
    }

    @Test
    void changeInfo() {
        User user = createUser();
        setUpAuthentication(user);
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);

        ContactInfoOfUser contactInfoOfUser = new ContactInfoOfUser("NewName", LocalDate.now());

        user.setName(contactInfoOfUser.name());
        user.setBirthDay(contactInfoOfUser.birthDay());

        Mockito.when(userRepository.save(user)).thenReturn(user);

        Assertions.assertEquals(userService.changeInfo(user.getId(), contactInfoOfUser), user);
    }

    @Test
    void changeDetailInfo() {
        User user = createUser();
        setUpAuthentication(user);
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);

        DetailInfoOfUser detailInfoOfUser = new DetailInfoOfUser("newmail@gmail.com","+71234567890");

        user.setEmail(detailInfoOfUser.email());
        user.setPhoneNumber(detailInfoOfUser.phoneNumber());

        Mockito.when(userRepository.save(user)).thenReturn(user);

        Assertions.assertEquals(userService.changeDetailInfo(user.getId(), detailInfoOfUser), user);
    }

    @Test
    void changePhoto() {
        User user = createUser();
        setUpAuthentication(user);
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getContentType()).thenReturn("image/png");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        Assertions.assertEquals(userService.changePhoto(user.getId(), multipartFile), user);
    }

    @Test
    void deleteUserById() {
        User admin = createAdmin();
        setUpAuthentication(admin);
        Mockito.when(userRepository.findUserById(admin.getId())).thenReturn(admin);
        Assertions.assertEquals(userService.deleteUserById(admin.getId()), admin);
    }

    @Test
    void deletePhoto() {
        User user = createUser();
        setUpAuthentication(user);
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        Assertions.assertEquals(userService.deletePhoto(user.getId()), user);
    }



    private void setUpAuthentication(User user) {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(user.getName());
    }

    private void setUpAuthenticationFail(User user) {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("DifferentUser");
    }

    private User createUser() {
        return new User(2L,
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

    private UserDTO createDtoAdmin() {
        return new UserDTO(
                "RoleAdmin",
                "12345",
                LocalDate.now(),
                "RoleAdmin@mail.com",
                "+79811234567");
    }
}
