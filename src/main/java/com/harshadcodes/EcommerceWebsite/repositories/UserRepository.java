package com.harshadcodes.EcommerceWebsite.repositories;

import com.harshadcodes.EcommerceWebsite.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(@Email(message = "plz enter a valid email") String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(@NotBlank String username);
}
