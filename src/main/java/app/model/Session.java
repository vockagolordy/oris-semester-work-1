package app.model;

import java.time.LocalDateTime;

public class Session {
    private String sessionId;
    private int userId;
    private LocalDateTime expireAt;

    public Session(String sessionId, int userId, LocalDateTime expireAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.expireAt = expireAt;
    }

    public Session() {}

    //Геттеры и сеттеры
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }
    public void setExpireAt(LocalDateTime expireAt) { this.expireAt = expireAt; }
}
