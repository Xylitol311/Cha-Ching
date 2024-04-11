package com.example.chaching.user.repository;


import com.example.chaching.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findByUserId(String username);

}
