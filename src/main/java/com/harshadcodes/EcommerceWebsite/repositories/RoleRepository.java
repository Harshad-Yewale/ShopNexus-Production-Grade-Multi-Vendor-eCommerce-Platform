package com.harshadcodes.EcommerceWebsite.repositories;

import com.harshadcodes.EcommerceWebsite.constants.AppRole;
import com.harshadcodes.EcommerceWebsite.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRole(AppRole appRole);
}
