package com.example.userphoto.services;

import com.example.userphoto.Util.UserToUserDTO;
import com.example.userphoto.exceptions.ErrorInputFile;
import com.example.userphoto.exceptions.ErrorUserDoesNotExist;
import com.example.userphoto.exceptions.ErrorUserForbidden;
import com.example.userphoto.models.*;
import com.example.userphoto.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserToUserDTO mapper;


    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public UserDTO findUserById(Long id) {
        User user = getUser(id);
        return mapper.toUserDTO(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User postUser(UserDTO userDTO, MultipartFile file) {
        User user = mapper.toUser(userDTO);
        try {
            if (!isValid(file)){
                throw new ErrorInputFile();
            }
            user.setPhoto(file.getBytes());
        } catch (IOException e) {
            throw new ErrorInputFile();
        }
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public User changeInfo(Long id, ContactInfoOfUser contactInfo) {
        User user = getUser(id);
        user.setName(contactInfo.name());
        user.setBirthDay(contactInfo.birthDay());
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public User changeDetailInfo(Long id, DetailInfoOfUser detailInfo) {
        User user = getUser(id);
        user.setEmail(detailInfo.email());
        user.setPhoneNumber(detailInfo.phoneNumber());
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public User changePhoto(Long id, MultipartFile file) {
        User user = getUser(id);
        try {
            if (!isValid(file)){
                throw new ErrorInputFile();
            }
            user.setPhoto(file.getBytes());
        } catch (IOException e) {
            throw new ErrorInputFile();
        }
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public User deleteUserById(Long id) {
        User user = getUser(id);
        userRepository.delete(user);
        return user;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public User deletePhoto(Long id) {
        User user = getUser(id);
        user.setPhoto(null);
        return userRepository.save(user);
    }


    public boolean isValid(MultipartFile multipartFile) {
        boolean result = true;
        String contentType = multipartFile.getContentType();
        Objects.requireNonNull(contentType,"contentType must not be null!");
        if (!isSupportedContentType(contentType)) {
            result = false;
        }
        return result;
    }

    public boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

    public User checkUserIsPresentById (Long id){
        User user = userRepository.findUserById(id);
        if (user == null){
            throw new ErrorUserDoesNotExist();
        }
        return user;
    }

    public User getUser(Long id) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = checkUserIsPresentById(id);
        if (!name.equals(user.getName())){
            throw new ErrorUserForbidden();
        }
        return user;
    }

}
