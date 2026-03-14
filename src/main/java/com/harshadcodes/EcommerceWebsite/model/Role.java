package com.harshadcodes.EcommerceWebsite.model;

import com.harshadcodes.EcommerceWebsite.constants.AppRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "roles_tbl")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @ToString.Exclude
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20,name = "role_name")
    private AppRole role;

    public Role(AppRole role) {
        this.role = role;
    }
}
