package app.repository;

import app.model.Contribution;
import app.database.DatabaseConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContributionRepositoryJdbc implements ContributionRepository {

    @Override
    public Contribution save(Contribution contribution) {
        String sql = "INSERT INTO contributions (goal_id, user_id, amount, contributed_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, contribution.getGoalId());
            stmt.setInt(2, contribution.getUserId());
            stmt.setDouble(3, contribution.getAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(contribution.getContributedAt()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        contribution.setId(rs.getInt(1));
                        return contribution;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Contribution> findById(int id) {
        String sql = "SELECT * FROM contributions WHERE id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToContribution(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Contribution> findAll() {
        List<Contribution> contributions = new ArrayList<>();
        String sql = "SELECT * FROM contributions ORDER BY contributed_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contributions.add(mapResultSetToContribution(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contributions;
    }

    @Override
    public List<Contribution> findByGoalId(int goalId) {
        List<Contribution> contributions = new ArrayList<>();
        String sql = "SELECT * FROM contributions WHERE goal_id = ? ORDER BY contributed_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, goalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contributions.add(mapResultSetToContribution(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contributions;
    }

    @Override
    public List<Contribution> findByUserId(int userId) {
        List<Contribution> contributions = new ArrayList<>();
        String sql = "SELECT * FROM contributions WHERE user_id = ? ORDER BY contributed_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contributions.add(mapResultSetToContribution(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contributions;
    }

    @Override
    public List<Contribution> findByUserAndGoal(int userId, int goalId) {
        List<Contribution> contributions = new ArrayList<>();
        String sql = "SELECT * FROM contributions WHERE user_id = ? AND goal_id = ? ORDER BY contributed_at DESC";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, goalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contributions.add(mapResultSetToContribution(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contributions;
    }

    @Override
    public double getTotalContributionsByGoal(int goalId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM contributions WHERE goal_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, goalId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public double getTotalContributionsByUser(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM contributions WHERE user_id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public List<Object[]> getContributionStatsByGoal(int goalId) {
        List<Object[]> stats = new ArrayList<>();
        String sql = """
            SELECT u.name as user_name, COALESCE(SUM(c.amount), 0) as total_contributed 
            FROM users u 
            LEFT JOIN contributions c ON u.id = c.user_id AND c.goal_id = ? 
            JOIN group_members gm ON u.id = gm.user_id 
            JOIN goals g ON g.group_id = gm.group_id 
            WHERE g.id = ? 
            GROUP BY u.id, u.name 
            ORDER BY total_contributed DESC
            """;

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, goalId);
            stmt.setInt(2, goalId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String userName = rs.getString("user_name");
                    double totalContributed = rs.getDouble("total_contributed");
                    stats.add(new Object[]{userName, totalContributed});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM contributions WHERE id = ?";

        try (Connection conn = DatabaseConnect.get();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Contribution mapResultSetToContribution(ResultSet rs) throws SQLException {
        Contribution contribution = new Contribution();
        contribution.setId(rs.getInt("id"));
        contribution.setGoalId(rs.getInt("goal_id"));
        contribution.setUserId(rs.getInt("user_id"));
        contribution.setAmount(rs.getDouble("amount"));
        contribution.setContributedAt(rs.getTimestamp("contributed_at").toLocalDateTime());
        return contribution;
    }
}