package app.service;

import app.model.Goal;
import app.model.Contribution;
import app.repository.GoalRepository;
import app.repository.ContributionRepository;
import app.repository.GroupMemberRepository;
import java.util.List;

public class GoalService {
    private final GoalRepository goalRepository;
    private final ContributionRepository contributionRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GoalService(GoalRepository goalRepository,
                       ContributionRepository contributionRepository,
                       GroupMemberRepository groupMemberRepository) {
        this.goalRepository = goalRepository;
        this.contributionRepository = contributionRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public Goal createPersonalGoal(int userId, String name, double targetAmount) {
        Goal goal = new Goal(name, targetAmount, userId);
        return goalRepository.save(goal);
    }

    public Goal createGroupGoal(int groupId, String name, double targetAmount) {
        Goal existingGoal = goalRepository.findByGroupIdSingle(groupId);
        if (existingGoal != null) {
            throw new IllegalStateException("У группы уже есть активная цель");
        }

        Goal goal = new Goal(groupId, name, targetAmount);
        return goalRepository.save(goal);
    }

    public boolean updateGroupGoal(int groupId, String name, double targetAmount) {
        Goal goal = goalRepository.findByGroupIdSingle(groupId);
        if (goal == null) {
            throw new IllegalStateException("У группы нет цели");
        }

        goal.setName(name);
        goal.setTargetAmount(targetAmount);
        return goalRepository.update(goal);
    }

    public List<Goal> getPersonalGoals(int userId) {
        return goalRepository.findByUserId(userId);
    }

    public List<Goal> getGroupGoals(int userId) {
        return groupMemberRepository.findByUserId(userId).stream()
                .map(member -> goalRepository.findByGroupIdSingle(member.getGroupId()))
                .filter(goal -> goal != null)
                .toList();
    }

    public Contribution contributeToGoal(int goalId, int userId, double amount) {
        Goal goal = goalRepository.findById(goalId);
        if (goal == null) {
            throw new IllegalArgumentException("Goal not found");
        }

        if (goal.isGroupGoal()) {
            boolean isMember = groupMemberRepository.existsByUserAndGroup(userId, goal.getGroupId());
            if (!isMember) {
                throw new SecurityException("User is not a member of the group");
            }
        } else {
            if (goal.getUserId() != userId) {
                throw new SecurityException("User is not the owner of this goal");
            }
        }

        Contribution contribution = new Contribution(goalId, userId, amount);
        return contributionRepository.save(contribution);
    }

    public List<Object[]> getContributionStats(int goalId) {
        return contributionRepository.getContributionStatsByGoal(goalId);
    }

    public double getGoalProgress(int goalId) {
        Goal goal = goalRepository.findById(goalId);
        if (goal == null) {
            throw new IllegalArgumentException("Goal not found");
        }
        return goal.getProgressPercentage();
    }

    public Goal getGoalById(int goalId) {
        return goalRepository.findById(goalId);
    }

    public boolean deleteGoal(int goalId) {
        return goalRepository.delete(goalId);
    }
}