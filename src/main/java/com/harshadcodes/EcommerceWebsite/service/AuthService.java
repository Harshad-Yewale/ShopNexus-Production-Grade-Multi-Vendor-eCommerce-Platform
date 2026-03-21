package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.payload.LoginRequest;
import com.harshadcodes.EcommerceWebsite.payload.SignupRequest;
import com.harshadcodes.EcommerceWebsite.payload.SignupResponse;
import com.harshadcodes.EcommerceWebsite.payload.UserInfoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    public UserInfoResponse SignIn(@Valid LoginRequest request);

    SignupResponse signUp(@Valid SignupRequest signupRequest);
}
