package app.repository;

import app.model.Group;
import java.util.List;

public interface GroupRepository {
    Group save(Group group);
    Group findById(int id);
    List<Group> findAll();
    List<Group> findByUserId(int userId);
    List<Group> findByCreatorId(int creatorId);
    boolean update(Group group);
    boolean delete(int id);
    boolean existsByName(String name);
}