package app.repository;

import app.model.Group;
import app.database.DatabaseConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupRepositoryJdbc implements GroupRepository {

    @Override
    public Group save(Group group) {
        String sql = "INSERT INTO groups (name, description, created_by, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, group.getName());
            stmt.setString(2, group.getDescription());
            stmt.setInt(3, group.getCreatedBy());
            stmt.setTimestamp(4, Timestamp.valueOf(group.getCreatedAt()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        group.setId(rs.getInt(1));
                        return group;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Group findById(int id) {
        String sql = "SELECT * FROM groups WHERE id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGroup(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM groups ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                groups.add(mapResultSetToGroup(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public List<Group> findByUserId(int userId) {
        List<Group> groups = new ArrayList<>();
        String sql = """
            SELECT g.* FROM groups g 
            JOIN group_members gm ON g.id = gm.group_id 
            WHERE gm.user_id = ? 
            ORDER BY g.created_at DESC
            """;

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(mapResultSetToGroup(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public List<Group> findByCreatorId(int creatorId) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM groups WHERE created_by = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, creatorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    groups.add(mapResultSetToGroup(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    public boolean update(Group group) {
        String sql = "UPDATE groups SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, group.getName());
            stmt.setString(2, group.getDescription());
            stmt.setInt(3, group.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM groups WHERE id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM groups WHERE name = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Group mapResultSetToGroup(ResultSet rs) throws SQLException {
        Group group = new Group();
        group.setId(rs.getInt("id"));
        group.setName(rs.getString("name"));
        group.setDescription(rs.getString("description"));
        group.setCreatedBy(rs.getInt("created_by"));
        group.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return group;
    }
}