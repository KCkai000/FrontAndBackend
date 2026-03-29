package com.example.demo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneAndDeletedFalse(String phone);

    boolean existsByPhoneAndDeletedFalse(String phone);

    boolean existsByEmailAndDeletedFalse(String email);
}

