package com.example.financeManager.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financeManager.dto.Expense;
import com.example.financeManager.dto.User;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserAndCategoryContainingIgnoreCase(User user, String category);
}