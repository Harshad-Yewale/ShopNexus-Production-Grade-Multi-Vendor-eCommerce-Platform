package com.harshadcodes.EcommerceWebsite.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record SignupRequest(

        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,
        Set<String> roles
) {
}
