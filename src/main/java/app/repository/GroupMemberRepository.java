package app.repository;

import app.model.GroupMember;
import java.util.List;

public interface GroupMemberRepository {
    GroupMember save(GroupMember groupMember);
    GroupMember findById(int id);
    GroupMember findByUserAndGroup(int userId, int groupId);
    List<GroupMember> findAll();
    List<GroupMember> findByUserId(int userId);
    List<GroupMember> findByGroupId(int groupId);
    List<GroupMember> findByGroupIdWithRole(int groupId, String role);
    boolean updateRole(int userId, int groupId, String newRole);
    boolean delete(int id);
    boolean deleteByUserAndGroup(int userId, int groupId);
    boolean existsByUserAndGroup(int userId, int groupId);
    boolean update(GroupMember groupMember);
    void deleteByGroupAndUser(int groupId, int userId);
    int countByGroupId(int groupId);
    GroupMember getOldestMemberAfterCreator(int groupId);
}