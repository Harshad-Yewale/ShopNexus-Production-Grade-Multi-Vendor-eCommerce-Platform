package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.constants.AppRole;
import com.harshadcodes.EcommerceWebsite.exceptions.ResourceAlreadyExistException;
import com.harshadcodes.EcommerceWebsite.exceptions.ResourceNotFoundException;
import com.harshadcodes.EcommerceWebsite.model.Role;
import com.harshadcodes.EcommerceWebsite.model.User;
import com.harshadcodes.EcommerceWebsite.payload.LoginRequest;
import com.harshadcodes.EcommerceWebsite.payload.SignupRequest;
import com.harshadcodes.EcommerceWebsite.payload.SignupResponse;
import com.harshadcodes.EcommerceWebsite.payload.UserInfoResponse;
import com.harshadcodes.EcommerceWebsite.repositories.RoleRepository;
import com.harshadcodes.EcommerceWebsite.repositories.UserRepository;
import com.harshadcodes.EcommerceWebsite.security.jwt.JwtUtils;
import com.harshadcodes.EcommerceWebsite.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{


    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserInfoResponse SignIn(LoginRequest loginRequest) {

        Authentication authentication;

        authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie cookie = jwtUtils.generateJwtFromCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new  UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles,cookie.toString());
    }

    @Override
    public SignupResponse signUp(SignupRequest signupRequest) {
        if(userRepository.existsByEmail(signupRequest.email())){
            throw new ResourceAlreadyExistException("User","email",signupRequest.email());
        }
        if(userRepository.existsByUsername(signupRequest.username())){
            throw new ResourceAlreadyExistException("User","username",signupRequest.username());
        }
        String encodedPassword=passwordEncoder.encode(signupRequest.password());
        User savedUser=new User(signupRequest.username(),signupRequest.email(),encodedPassword);

        Set<String> strRoles=signupRequest.roles();
        Set<Role> roles=new HashSet<>();

        if(strRoles==null){
            Role userRole=roleRepository.findByRole(AppRole.ROLE_USER).orElseThrow(()->new ResourceNotFoundException("Role","User",strRoles.toString()));
            roles.add(userRole);
        }
        else{
            strRoles.forEach(role->{
                    switch (role.toUpperCase()){

                        case "ADMIN":
                            Role adminRole=roleRepository.findByRole(AppRole.ROLE_ADMIN).orElseThrow(()->new ResourceNotFoundException("Role","User",role));
                            roles.add(adminRole);
                            break;
                        case "SELLER":
                            Role sellerRole=roleRepository.findByRole(AppRole.ROLE_SELLER).orElseThrow(()->new ResourceNotFoundException("Role","User",role));
                            roles.add(sellerRole);
                            break;
                        default:
                            Role userRole=roleRepository.findByRole(AppRole.ROLE_USER).orElseThrow(()->new ResourceNotFoundException("Role","User",role));
                            roles.add(userRole);
                            break;
                    };
            });
        }
        savedUser.setUserRoles(roles);
        userRepository.save(savedUser);
        String message="welcome "+savedUser.getUsername()+" your registration was successful";
        String roleMessage="You are : "+savedUser.getUserRoles().toString();

       return  new SignupResponse(message,roleMessage);
    }
}
