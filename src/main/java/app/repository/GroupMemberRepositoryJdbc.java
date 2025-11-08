package app.repository;

import app.model.GroupMember;
import app.database.DatabaseConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberRepositoryJdbc implements GroupMemberRepository {

    @Override
    public GroupMember save(GroupMember groupMember) {
        String sql = "INSERT INTO group_members (user_id, group_id, role, joined_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, groupMember.getUserId());
            stmt.setInt(2, groupMember.getGroupId());
            stmt.setString(3, groupMember.getRole());
            stmt.setTimestamp(4, Timestamp.valueOf(groupMember.getJoinedAt()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        groupMember.setId(rs.getInt(1));
                        return groupMember;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GroupMember findById(int id) {
        String sql = "SELECT * FROM group_members WHERE id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGroupMember(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GroupMember findByUserAndGroup(int userId, int groupId) {
        String sql = "SELECT * FROM group_members WHERE user_id = ? AND group_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGroupMember(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<GroupMember> findAll() {
        List<GroupMember> members = new ArrayList<>();
        String sql = "SELECT * FROM group_members ORDER BY joined_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapResultSetToGroupMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public List<GroupMember> findByUserId(int userId) {
        List<GroupMember> members = new ArrayList<>();
        String sql = "SELECT * FROM group_members WHERE user_id = ? ORDER BY joined_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToGroupMember(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public List<GroupMember> findByGroupId(int groupId) {
        List<GroupMember> members = new ArrayList<>();
        String sql = "SELECT * FROM group_members WHERE group_id = ? ORDER BY joined_at ASC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToGroupMember(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public List<GroupMember> findByGroupIdWithRole(int groupId, String role) {
        List<GroupMember> members = new ArrayList<>();
        String sql = "SELECT * FROM group_members WHERE group_id = ? AND role = ? ORDER BY joined_at ASC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            stmt.setString(2, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapResultSetToGroupMember(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    @Override
    public boolean updateRole(int userId, int groupId, String newRole) {
        String sql = "UPDATE group_members SET role = ? WHERE user_id = ? AND group_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newRole);
            stmt.setInt(2, userId);
            stmt.setInt(3, groupId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM group_members WHERE id = ?";

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
    public boolean deleteByUserAndGroup(int userId, int groupId) {
        String sql = "DELETE FROM group_members WHERE user_id = ? AND group_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, groupId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existsByUserAndGroup(int userId, int groupId) {
        String sql = "SELECT 1 FROM group_members WHERE user_id = ? AND group_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(GroupMember groupMember) {
        String sql = "UPDATE group_members SET user_id = ?, group_id = ?, role = ?, joined_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupMember.getUserId());
            stmt.setInt(2, groupMember.getGroupId());
            stmt.setString(3, groupMember.getRole());
            stmt.setTimestamp(4, Timestamp.valueOf(groupMember.getJoinedAt()));
            stmt.setInt(5, groupMember.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void deleteByGroupAndUser(int groupId, int userId) {
        String sql = "DELETE FROM group_members WHERE group_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int countByGroupId(int groupId) {
        String sql = "SELECT COUNT(*) as member_count FROM group_members WHERE group_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("member_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public GroupMember getOldestMemberAfterCreator(int groupId) {
        String sql = """
            SELECT * FROM group_members 
            WHERE group_id = ? AND role != 'creator' 
            ORDER BY joined_at ASC 
            LIMIT 1
            """;

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGroupMember(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private GroupMember mapResultSetToGroupMember(ResultSet rs) throws SQLException {
        GroupMember member = new GroupMember();
        member.setId(rs.getInt("id"));
        member.setUserId(rs.getInt("user_id"));
        member.setGroupId(rs.getInt("group_id"));
        member.setRole(rs.getString("role"));
        member.setJoinedAt(rs.getTimestamp("joined_at").toLocalDateTime());
        return member;
    }
}