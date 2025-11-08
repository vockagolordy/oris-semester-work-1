package app.model;

import java.time.LocalDateTime;

public class GroupMember {
    private int id;
    private int userId;
    private int groupId;
    private String role; // Создатель, участник, админ
    private LocalDateTime joinedAt;

    public GroupMember() {}

    public GroupMember(int userId, int groupId, String role) {
        this.userId = userId;
        this.groupId = groupId;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    // Вспомогательные методы
    public boolean isCreator() {
        return "creator".equalsIgnoreCase(role);
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role) || isCreator();
    }

    @Override
    public String toString() {
        return "GroupMember{id=" + id + ", userId=" + userId + ", groupId=" + groupId + ", role='" + role + "'}";
    }
}