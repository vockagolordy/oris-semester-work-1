package app.repository;

import app.model.Session;
import java.time.LocalDateTime;

public interface SessionRepository {
    void addSession(int userId, String sessionId, LocalDateTime expireAt);
    Session findBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
}