package app.repository;

import app.model.Goal;
import java.util.List;

public interface GoalRepository {
    Goal save(Goal goal);
    Goal findById(int id);
    List<Goal> findAll();
    List<Goal> findByGroupId(int groupId);
    List<Goal> findByUserId(int userId);
    Goal findByGroupIdSingle(int groupId);
    boolean update(Goal goal);
    boolean updateCurrentAmount(int goalId, double newAmount);
    boolean delete(int id);
    boolean deleteByGroupId(int groupId);
}