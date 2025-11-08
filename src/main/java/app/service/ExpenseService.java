package app.service;

import app.model.Expense;
import app.repository.ExpenseRepository;
import java.util.List;

public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(int userId, String type, String category, double amount, String description) {
        Expense expense = new Expense(userId, type, category, amount, description);
        return expenseRepository.save(expense);
    }

    public List<Expense> getUserExpenses(int userId) {
        return expenseRepository.findByUserId(userId);
    }

    public List<Expense> getUserExpensesByType(int userId, String type) {
        return expenseRepository.findByUserIdAndType(userId, type);
    }

    public double getUserBalance(int userId) {
        return expenseRepository.getBalanceByUser(userId);
    }

    public double getTotalIncome(int userId) {
        return expenseRepository.getTotalIncomeByUser(userId);
    }

    public double getTotalExpenses(int userId) {
        return expenseRepository.getTotalExpensesByUser(userId);
    }

    public boolean deleteExpense(int expenseId) {
        return expenseRepository.delete(expenseId);
    }

    public List<Expense> getExpensesByCategory(int userId, String category) {
        return expenseRepository.findByUserIdAndType(userId, "EXPENSE").stream()
                .filter(expense -> category.equals(expense.getCategory()))
                .toList();
    }
}