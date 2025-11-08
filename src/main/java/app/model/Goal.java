package app.model;

import java.time.LocalDateTime;

public class Goal {
    private int id;
    private Integer groupId; // Может быть null
    private Integer userId;  // Может быть null
    private String name;
    private double targetAmount;
    private double currentAmount;
    private LocalDateTime createdAt;

    public Goal() {}

    // Конструктор для групповой цели
    public Goal(Integer groupId, String name, double targetAmount) {
        this.groupId = groupId;
        this.userId = null;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = 0.0;
        this.createdAt = LocalDateTime.now();
    }

    // Конструктор для личной цели
    public Goal(String name, double targetAmount, Integer userId) {
        this.userId = userId;
        this.groupId = null;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = 0.0;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getGroupId() { return groupId; }
    public void setGroupId(Integer groupId) { this.groupId = groupId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }

    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Дополнительные методы
    public double getProgressPercentage() {
        return targetAmount > 0 ? (currentAmount / targetAmount) * 100 : 0;
    }

    public double getRemainingAmount() {
        return targetAmount - currentAmount;
    }

    public boolean isPersonalGoal() {
        return userId != null && groupId == null;
    }

    public boolean isGroupGoal() {
        return groupId != null && userId == null;
    }

    @Override
    public String toString() {
        return "Goal{id=" + id + ", name='" + name + "', progress=" + getProgressPercentage() + "%}";
    }
}