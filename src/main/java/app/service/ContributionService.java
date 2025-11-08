package app.service;

import app.model.Contribution;
import app.repository.ContributionRepository;
import java.util.List;

public class ContributionService {
    private final ContributionRepository contributionRepository;

    public ContributionService(ContributionRepository contributionRepository) {
        this.contributionRepository = contributionRepository;
    }

    public List<Contribution> getContributionsByGoal(int goalId) {
        return contributionRepository.findByGoalId(goalId);
    }

    public List<Contribution> getContributionsByUser(int userId) {
        return contributionRepository.findByUserId(userId);
    }

    public double getTotalContributionsByGoal(int goalId) {
        return contributionRepository.getTotalContributionsByGoal(goalId);
    }

    public double getTotalContributionsByUser(int userId) {
        return contributionRepository.getTotalContributionsByUser(userId);
    }

    public List<Object[]> getContributionStatsByGoal(int goalId) {
        return contributionRepository.getContributionStatsByGoal(goalId);
    }

    public boolean deleteContribution(int contributionId) {
        return contributionRepository.delete(contributionId);
    }
}