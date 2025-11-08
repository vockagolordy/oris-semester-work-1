package app.repository;

import app.model.Expense;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    Expense save(Expense expense);
    Optional<Expense> findById(int id);
    List<Expense> findAll();
    List<Expense> findByUserId(int userId);
    List<Expense> findByUserIdAndType(int userId, String type);
    List<Expense> findByCategory(String category);
    List<Expense> findByDateRange(int userId, java.time.LocalDate start, java.time.LocalDate end);
    double getTotalIncomeByUser(int userId);
    double getTotalExpensesByUser(int userId);
    double getBalanceByUser(int userId);
    boolean update(Expense expense);
    boolean delete(int id);
}