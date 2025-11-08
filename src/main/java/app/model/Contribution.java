package app.model;

import java.time.LocalDateTime;

public class Contribution {
    private int id;
    private int goalId;
    private int userId;
    private double amount;
    private LocalDateTime contributedAt;

    public Contribution() {}

    public Contribution(int goalId, int userId, double amount) {
        this.goalId = goalId;
        this.userId = userId;
        this.amount = amount;
        this.contributedAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getGoalId() { return goalId; }
    public void setGoalId(int goalId) { this.goalId = goalId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getContributedAt() { return contributedAt; }
    public void setContributedAt(LocalDateTime contributedAt) { this.contributedAt = contributedAt; }

    @Override
    public String toString() {
        return "Contribution{id=" + id + ", userId=" + userId + ", goalId=" + goalId + ", amount=" + amount + "}";
    }
}