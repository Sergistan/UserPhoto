package com.example.userphoto.controllers;

import com.example.userphoto.models.ContactInfoOfUser;
import com.example.userphoto.models.DetailInfoOfUser;
import com.example.userphoto.models.User;
import com.example.userphoto.models.UserDTO;
import com.example.userphoto.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> allUsers() {
        List<User> users = userService.allUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        UserDTO userById = userService.findUserById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> postUser(@Valid @RequestPart("user") UserDTO userDTO,
                                      @NotNull @RequestPart("photo") MultipartFile file) {
        User user = userService.postUser(userDTO, file);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/change_info/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changeInfo(@PathVariable Long id,
                                        @Valid @RequestBody ContactInfoOfUser contactInfo) {
        User user = userService.changeInfo(id, contactInfo);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/change_detail_info/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changeDetailInfo(@PathVariable Long id,
                                              @Valid @RequestBody DetailInfoOfUser detailInfo) {
        User user = userService.changeDetailInfo(id, detailInfo);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PutMapping("/change_photo/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changePhoto(@PathVariable Long id,
                                         @NotNull @RequestParam("photo") MultipartFile photo) {
        User user = userService.changePhoto(id, photo);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        User user = userService.deleteUserById(id);
        return new ResponseEntity<>(String.format("%s deleted!", user.getName()), HttpStatus.OK);
    }


    @DeleteMapping("/delete_photo/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        User user = userService.deletePhoto(id);
        return new ResponseEntity<>(String.format("%s's photo deleted!", user.getName()), HttpStatus.OK);
    }
}
