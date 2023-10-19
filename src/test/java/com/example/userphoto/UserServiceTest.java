package com.example.userphoto;

import com.example.userphoto.Util.UserToUserDTO;
import com.example.userphoto.models.ContactInfoOfUser;
import com.example.userphoto.models.DetailInfoOfUser;
import com.example.userphoto.repositories.UserRepository;
import com.example.userphoto.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

import static com.example.userphoto.TestData.*;

@ExtendWith(MockitoExtension.class)
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
        Mockito.when(userRepository.findAll()).thenReturn(List.of(USER_WITH_ROLE_ADMIN));
        Assertions.assertEquals(userService.allUsers(), List.of(USER_WITH_ROLE_ADMIN));
    }

    @Test
    void findUserById() {
        Mockito.when(userRepository.findUserById(USER_WITH_ROLE_ADMIN.getId())).thenReturn(USER_WITH_ROLE_ADMIN);
        setUpAuthenticationAdmin();
        Mockito.when(mapper.toUserDTO(USER_WITH_ROLE_ADMIN)).thenReturn(USER_WITH_ROLE_ADMIN_DTO);
        Assertions.assertEquals(userService.findUserById(USER_WITH_ROLE_ADMIN.getId()), USER_WITH_ROLE_ADMIN_DTO);
    }

    @Test
    void postUser() {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

        Mockito.when(mapper.toUser(USER_WITH_ROLE_ADMIN_DTO)).thenReturn(USER_WITH_ROLE_ADMIN);
        Mockito.when(multipartFile.getContentType()).thenReturn("image/png");
        Mockito.when(userRepository.save(USER_WITH_ROLE_ADMIN)).thenReturn(USER_WITH_ROLE_ADMIN);
        Mockito.when(passwordEncoder.encode(USER_WITH_ROLE_ADMIN_DTO.password())).thenReturn(String.valueOf(USER_WITH_ROLE_ADMIN_DTO));

        Assertions.assertEquals(userService.postUser(USER_WITH_ROLE_ADMIN_DTO, multipartFile), USER_WITH_ROLE_ADMIN);
    }

    @Test
    void changeInfo() {
        ContactInfoOfUser contactInfoOfUser = Mockito.mock(ContactInfoOfUser.class);
        Mockito.when(userRepository.findUserById(USER_WITH_ROLE_ADMIN.getId())).thenReturn(USER_WITH_ROLE_ADMIN);
        setUpAuthenticationAdmin();

        Mockito.when(userRepository.save(USER_WITH_ROLE_ADMIN)).thenReturn(USER_WITH_ROLE_ADMIN);

        Assertions.assertEquals(userService.changeInfo(USER_WITH_ROLE_ADMIN.getId(), contactInfoOfUser), USER_WITH_ROLE_ADMIN);
    }


    @Test
    void changeDetailInfo() {
        DetailInfoOfUser detailInfoOfUser = Mockito.mock(DetailInfoOfUser.class);
        Mockito.when(userRepository.findUserById(USER_WITH_ROLE_ADMIN.getId())).thenReturn(USER_WITH_ROLE_ADMIN);
        setUpAuthenticationAdmin();

        Mockito.when(userRepository.save(USER_WITH_ROLE_ADMIN)).thenReturn(USER_WITH_ROLE_ADMIN);

        Assertions.assertEquals(userService.changeDetailInfo(USER_WITH_ROLE_ADMIN.getId(), detailInfoOfUser), USER_WITH_ROLE_ADMIN);
    }

    @Test
    void changePhoto() {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(userRepository.findUserById(USER_WITH_ROLE_ADMIN.getId())).thenReturn(USER_WITH_ROLE_ADMIN);
        setUpAuthenticationAdmin();

        Mockito.when(multipartFile.getContentType()).thenReturn("image/png");
        Mockito.when(userRepository.save(USER_WITH_ROLE_ADMIN)).thenReturn(USER_WITH_ROLE_ADMIN);

        Assertions.assertEquals(userService.changePhoto(USER_WITH_ROLE_ADMIN.getId(), multipartFile), USER_WITH_ROLE_ADMIN);
    }

    @Test
    void deleteUserById() {
        Mockito.when(userRepository.findUserById(USER_WITH_ROLE_ADMIN.getId())).thenReturn(USER_WITH_ROLE_ADMIN);
        setUpAuthenticationAdmin();

        Assertions.assertEquals(userService.deleteUserById(USER_WITH_ROLE_ADMIN.getId()), USER_WITH_ROLE_ADMIN);
    }

    @Test
    void deletePhoto() {
        Mockito.when(userRepository.findUserById(USER_WITH_ROLE_ADMIN.getId())).thenReturn(USER_WITH_ROLE_ADMIN);
        setUpAuthenticationAdmin();
        Mockito.when(userRepository.save(USER_WITH_ROLE_ADMIN)).thenReturn(USER_WITH_ROLE_ADMIN_WITHOUT_PHOTO);

        Assertions.assertEquals(userService.deletePhoto(USER_WITH_ROLE_ADMIN.getId()), USER_WITH_ROLE_ADMIN_WITHOUT_PHOTO);
    }


    private void setUpAuthenticationAdmin() {
        Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }
            @Override
            public Object getCredentials() {
                return null;
            }
            @Override
            public Object getDetails() {
                return null;
            }
            @Override
            public Object getPrincipal() {
                return null;
            }
            @Override
            public boolean isAuthenticated() {
                return false;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }
            @Override
            public String getName() {
                return USER_WITH_ROLE_ADMIN.getName();
            }
        });
        SecurityContextHolder.setContext(securityContext);
    }
}
