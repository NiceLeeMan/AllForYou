package com.example.sidedemo.repository;


import com.example.sidedemo.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUserId(String userId);
    boolean existsUserByEmail(String email);

    Optional<User> findByUserId(String userId);
}