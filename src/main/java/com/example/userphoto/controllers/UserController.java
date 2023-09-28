package com.example.userphoto.controllers;

import com.example.userphoto.models.ContactInfoOfUser;
import com.example.userphoto.models.DetailInfoOfUser;
import com.example.userphoto.models.User;
import com.example.userphoto.models.UserDTO;
import com.example.userphoto.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user_photo")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<User> allUsers() {
        return userService.allUsers();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/users/{id}")
    public UserDTO allUsers(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<?> postUser(@RequestBody UserDTO userDTO) {
        User user = userService.postUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/users/change_info/{id}")
    public ResponseEntity<?> changeInfo(@PathVariable Long id, @RequestBody ContactInfoOfUser contactInfo) {
        User user = userService.changeInfo(id, contactInfo);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/users/change_detail_info/{id}")
    public ResponseEntity<?> changeDetailInfo(@PathVariable Long id, @RequestBody DetailInfoOfUser detailInfo) {
        User user = userService.changeDetailInfo(id, detailInfo);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/users/change_photo/{id}")
    public ResponseEntity<?> changePhoto(@PathVariable Long id, MultipartFile file) {
        User user = userService.changePhoto(id, file);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @DeleteMapping ("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        User user = userService.deleteUserById(id);
        return new ResponseEntity<>(user.getName() + " deleted!", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping ("/users/delete_photo/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        User user = userService.deletePhoto(id);
        return new ResponseEntity<>(user.getName() + " photo deleted!", HttpStatus.OK);
    }
}
