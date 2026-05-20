package com.example.financeManager.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeManager.dto.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}