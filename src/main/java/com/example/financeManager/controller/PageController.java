package com.example.financeManager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.financeManager.dto.Expense;
import com.example.financeManager.dto.Income;
import com.example.financeManager.dto.User;
import com.example.financeManager.repository.ExpenseRepository;
import com.example.financeManager.repository.IncomeRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) return "redirect:/login";

        List<Expense> expenses = expenseRepository.findByUser(user);
        List<Income> incomes = incomeRepository.findByUser(user);

        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("balance", totalIncome - totalExpense);
        List<Expense> recentExpenses = expenses.stream()
                .sorted((e1, e2) -> Long.compare(e2.getId(), e1.getId())) // latest first
                .limit(3)
                .toList();

        model.addAttribute("expenses", recentExpenses);

        // expense category
       
        Map<String, Double> categoryData = new HashMap<>();

        for (Expense e : expenses) {
            categoryData.put(
                e.getCategory(),
                categoryData.getOrDefault(e.getCategory(), 0.0) + e.getAmount()
            );
        }

        model.addAttribute("categoryData", categoryData);

        return "dashboard";
    }
    
    // FOR GOING TO EXPENSE PAGE
    
    @GetMapping("/add-expense")
    public String addExpensePage(Model model) {
        model.addAttribute("expense", new Expense());
        return "add-expense";
    }

    // ADD EXPENSE
    @PostMapping("/addExpense")
    public String addExpense(Expense expense, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) return "redirect:/login";

        expense.setUser(user);
        expenseRepository.save(expense);

        return "redirect:/expenses";
    }

    // VIEW EXPENSE
    @GetMapping("/expenses")
    public String expenses(@RequestParam(required = false) String category,
                           HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) return "redirect:/login";

        List<Expense> list;

        if (category != null && !category.isEmpty()) {
            list = expenseRepository.findByUserAndCategoryContainingIgnoreCase(user, category);
        } else {
            list = expenseRepository.findByUser(user);
        }
        
        model.addAttribute("expenses", list);
        return "expenses";
    }
    // FOR GOING TO ADD INCOME PAGE
    @GetMapping("/add-income")
    public String addIncomePage(Model model) {
        model.addAttribute("income", new Income());
        return "add-income";
    }
    // ADD INCOME
    @PostMapping("/addIncome")
    public String addIncome(Income income, HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) return "redirect:/login";

        income.setUser(user);
        incomeRepository.save(income);

        return "redirect:/income";
    }

    // VIEW INCOME
    @GetMapping("/income")
    public String income(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) return "redirect:/login";

        model.addAttribute("incomeList", incomeRepository.findByUser(user));

        return "income";
    }
    
    @PostMapping("/deleteExpense/{id}")
    public String deleteExpense(@PathVariable Long id,
                                HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) return "redirect:/login";

        // Optional safety: delete only if it belongs to logged user
        Expense expense = expenseRepository.findById(id).orElse(null);

        if (expense != null && expense.getUser().getId().equals(user.getId())) {
            expenseRepository.deleteById(id);
        }

        return "redirect:/expenses";
    }
    
    @PostMapping("/deleteIncome/{id}")
    public String deleteIncome(@PathVariable Long id,
                               HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");

        if (user == null) return "redirect:/login";

        Income income = incomeRepository.findById(id).orElse(null);

        if (income != null && income.getUser().getId().equals(user.getId())) {
            incomeRepository.deleteById(id);
        }

        return "redirect:/income";
    }
    
    
}