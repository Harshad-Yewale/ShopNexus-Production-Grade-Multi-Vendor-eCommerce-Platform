package com.harshadcodes.EcommerceWebsite.controller;


import com.harshadcodes.EcommerceWebsite.payload.LoginRequest;
import com.harshadcodes.EcommerceWebsite.payload.SignupRequest;
import com.harshadcodes.EcommerceWebsite.payload.SignupResponse;
import com.harshadcodes.EcommerceWebsite.payload.UserInfoResponse;
import com.harshadcodes.EcommerceWebsite.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?>authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        UserInfoResponse response=authService.SignIn(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> createUser(@Valid @RequestBody SignupRequest signupRequest){
        SignupResponse response= authService.signUp(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
