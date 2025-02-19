package com.example.sidedemo.repository;


import com.example.sidedemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUserId(String userId);
    boolean existsUserByEmail(String email);

}