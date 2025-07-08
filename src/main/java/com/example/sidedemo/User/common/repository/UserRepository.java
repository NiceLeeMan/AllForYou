package com.example.sidedemo.User.common.repository;


import com.example.sidedemo.User.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUserId(String userId);
    boolean existsUserByEmail(String email);
    Optional<User> findByUserId(String userId);
}