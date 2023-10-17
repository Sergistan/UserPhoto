package com.example.userphoto.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DetailInfoOfUser(@Email @NotBlank String email,
                               @Pattern(regexp = "^((\\+7|7|8)+([0-9]){10})$") @NotBlank String phoneNumber) {
}
