package com.example.financeManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeManager.dto.Income;
import com.example.financeManager.dto.User;


public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);
}