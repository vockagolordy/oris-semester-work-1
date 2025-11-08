package app.repository;

import app.model.Contribution;
import java.util.List;
import java.util.Optional;

public interface ContributionRepository {
    Contribution save(Contribution contribution);
    Optional<Contribution> findById(int id);
    List<Contribution> findAll();
    List<Contribution> findByGoalId(int goalId);
    List<Contribution> findByUserId(int userId);
    List<Contribution> findByUserAndGoal(int userId, int goalId);
    double getTotalContributionsByGoal(int goalId);
    double getTotalContributionsByUser(int userId);
    List<Object[]> getContributionStatsByGoal(int goalId); // Для пай-чарта
    boolean delete(int id);
}