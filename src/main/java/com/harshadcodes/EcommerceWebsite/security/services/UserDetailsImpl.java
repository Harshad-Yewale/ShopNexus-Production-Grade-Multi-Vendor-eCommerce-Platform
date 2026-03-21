package com.harshadcodes.EcommerceWebsite.security.services;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harshadcodes.EcommerceWebsite.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
public class UserDetailsImpl implements org.springframework.security.core.userdetails.UserDetails {

    private static final Long SerialVersionUID =1L;

    private Long id;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @JsonIgnore
    @NotBlank
    private String password;

    private Collection<? extends  GrantedAuthority> authorities;


    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user){

        List<GrantedAuthority> authorities=user.getUserRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());



        return new UserDetailsImpl(user.getId()
                ,user.getUsername()
                ,user.getEmail()
                , user.getPassword()
                ,authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(id, that.id);
    }
}
