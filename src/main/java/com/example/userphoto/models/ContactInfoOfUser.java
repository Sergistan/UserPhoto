package com.example.userphoto.models;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ContactInfoOfUser(@NotBlank @Size(min = 3, max = 15) String name,
                                @PastOrPresent @NotNull LocalDate birthDay) {
}
