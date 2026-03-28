package com.esun.socialmedia.domain.repository;

import com.esun.socialmedia.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneAndDeletedFalse(String phone);

    boolean existsByPhoneAndDeletedFalse(String phone);

    boolean existsByEmailAndDeletedFalse(String email);
}

