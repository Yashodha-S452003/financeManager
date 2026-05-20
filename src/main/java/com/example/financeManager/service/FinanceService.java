package com.example.financeManager.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.financeManager.dto.Expense;
import com.example.financeManager.dto.Income;
import com.example.financeManager.repository.ExpenseRepository;
import com.example.financeManager.repository.IncomeRepository;



@Service
public class FinanceService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private IncomeRepository incomeRepo;

    // Save Expense
    public void saveExpense(Expense expense) {
        expenseRepo.save(expense);
    }

    // Save Income
    public void saveIncome(Income income) {
        incomeRepo.save(income);
    }

    // Get all Expenses
    public List<Expense> getExpenses() {
        return expenseRepo.findAll();
    }

    // Get all Income
    public List<Income> getIncome() {
        return incomeRepo.findAll();
    }
    
    public void deleteExpense(Long id) {
        expenseRepo.deleteById(id);
    }
}