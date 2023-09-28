package com.example.userphoto.models;

import java.util.Date;

public record UserDTO(String name, String password, Date birthDay, String email, String phoneNumber, byte[] photo) {}


