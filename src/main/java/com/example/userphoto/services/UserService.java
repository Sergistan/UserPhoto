package com.example.userphoto.services;

import com.example.userphoto.Util.UserToUserDTO;
import com.example.userphoto.exceptions.ErrorInputFile;
import com.example.userphoto.exceptions.ErrorUserDoesNotExist;
import com.example.userphoto.exceptions.ErrorUserForbidden;
import com.example.userphoto.models.*;
import com.example.userphoto.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        User user = getUser(id);
        return mapper.toUserDTO(user);
    }

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

    public User changeInfo(Long id, ContactInfoOfUser contactInfo) {
        User user = getUser(id);
        user.setName(contactInfo.name());
        user.setBirthDay(contactInfo.birthDay());
        return userRepository.save(user);
    }

    public User changeDetailInfo(Long id, DetailInfoOfUser detailInfo) {
        User user = getUser(id);
        user.setEmail(detailInfo.email());
        user.setPhoneNumber(detailInfo.phoneNumber());
        return userRepository.save(user);
    }

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

    public User deleteUserById(Long id) {
        User user = getUser(id);
        userRepository.delete(user);
        return user;
    }

    public User deletePhoto(Long id) {
        User user = getUser(id);
        user.setPhoto(null);
        return userRepository.save(user);
    }


    private boolean isValid(MultipartFile multipartFile) {
        boolean result = true;
        String contentType = multipartFile.getContentType();
        Objects.requireNonNull(contentType,"contentType must not be null!");
        if (!isSupportedContentType(contentType)) {
            result = false;
        }
        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg")
                || contentType.equals("image/jpeg");
    }

    private User checkUserIsPresentById (Long id){
        User user = userRepository.findUserById(id);
        if (user == null){
            throw new ErrorUserDoesNotExist();
        }
        return user;
    }

    private User getUser(Long id) {
        User user = checkUserIsPresentById(id);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!name.equals(user.getName()) &&
                user.getAuthorities().contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name())) ){
            throw new ErrorUserForbidden();
        }
        return user;
    }

}
