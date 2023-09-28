package com.example.userphoto.services;

import com.example.userphoto.exceptions.ErrorInputFile;
import com.example.userphoto.models.*;
import com.example.userphoto.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static UserDTO convertToDTO(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }

    private static User convertToUser(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDTO, User.class);
    }
    @Transactional(readOnly = true)
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id) {
        return convertToDTO(userRepository.findUserById(id));
    }

    public User postUser(UserDTO userDTO) {
        User user = convertToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    public User changeInfo(Long id, ContactInfoOfUser contactInfo) {
        User user = userRepository.findUserById(id);
        user.setName(contactInfo.name());
        user.setBirthDay(contactInfo.birthDay());
        return userRepository.save(user);
    }

    public User changeDetailInfo(Long id, DetailInfoOfUser detailInfo) {
        User user = userRepository.findUserById(id);
        user.setEmail(detailInfo.email());
        user.setPhoneNumber(detailInfo.phoneNumber());
        return userRepository.save(user);
    }

    public User deleteUserById(Long id) {
        User user = userRepository.findUserById(id);
        userRepository.delete(user);
        return user;
    }

    public User changePhoto(Long id, MultipartFile file) {
        User user = userRepository.findUserById(id);
        try {
        user.setPhoto(file.getBytes());
        } catch (IOException e) {
            throw new ErrorInputFile();
        }
        return userRepository.save(user);
    }

    public User deletePhoto(Long id) {
        User user = userRepository.findUserById(id);
        user.setPhoto(null);
        return userRepository.save(user);
    }
}
