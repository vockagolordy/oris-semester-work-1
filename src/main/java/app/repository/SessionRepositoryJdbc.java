package app.repository;

import app.model.Session;
import app.database.DatabaseConnect;

import java.sql.*;
import java.time.LocalDateTime;

public class SessionRepositoryJdbc implements SessionRepository {

    @Override
    public void addSession(int userId, String sessionId, LocalDateTime expireAt) {
        String sql = "INSERT INTO sessions (session_id, user_id, expire_at) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            stmt.setInt(2, userId);
            stmt.setTimestamp(3, Timestamp.valueOf(expireAt));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Session findBySessionId(String sessionId) {
        String sql = "SELECT * FROM sessions WHERE session_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSession(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        String sql = "DELETE FROM sessions WHERE session_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sessionId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        return new Session(
                rs.getString("session_id"),
                rs.getInt("user_id"),
                rs.getTimestamp("expire_at").toLocalDateTime()
        );
    }
}