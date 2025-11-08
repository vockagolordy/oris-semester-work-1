package app.model;

import java.time.LocalDateTime;

public class Expense {
    private int id;
    private int userId;
    private String type; // Доход или расход
    private String category;
    private double amount;
    private String description;
    private LocalDateTime createdAt;

    public Expense() {}

    public Expense(int userId, String type, String category, double amount, String description) {
        this.userId = userId;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Вспомогательные методы
    public boolean isIncome() {
        return "INCOME".equalsIgnoreCase(type);
    }

    public boolean isExpense() {
        return "EXPENSE".equalsIgnoreCase(type);
    }

    @Override
    public String toString() {
        return "Expense{id=" + id + ", type='" + type + "', category='" + category + "', amount=" + amount + "}";
    }
}