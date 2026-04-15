package com.harshadcodes.EcommerceWebsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users_tbl")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @ToString.Exclude
    private Long id;

    @NotBlank(message = "username cannot be empty")
    @Column(length = 20,name = "username",nullable = false)
    private String username;


    @Email
    @NotBlank(message = "email cannot be empty")
    @Column(length = 120,name = "user_email",unique = true,nullable = false)
    private String email;


    @NotBlank(message = "password cannot be empty")
    @Size(min = 6,message = "password must be at least six characters long!!")
    @Column(name = "user_password",nullable = false)
    @JsonIgnore
    private String password;


    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}
                ,fetch = FetchType.LAZY
    )
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> userRoles=new HashSet<>();


    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.ALL},orphanRemoval = true,mappedBy = "user")
    private Set<Product> userProducts=new HashSet<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Cart cart;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}
            ,fetch = FetchType.LAZY
    )
    private List<Address> addresses=new ArrayList<>();


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
