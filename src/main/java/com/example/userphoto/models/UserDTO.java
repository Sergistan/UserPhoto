package com.example.userphoto.models;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserDTO(@NotBlank @Size(min = 3, max = 15) String name,
                      @NotBlank @Size(min = 3, max = 60) String password,
                      @PastOrPresent @NotNull LocalDate birthDay,
                      @Email @NotBlank String email,
                      @Pattern(regexp = "^((\\+7|7|8)+([0-9]){10})$") @NotBlank String phoneNumber) {}


