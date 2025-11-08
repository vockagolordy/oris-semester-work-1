package app.service;

import app.model.Group;
import app.model.GroupMember;
import app.model.Goal;
import app.model.User;
import app.repository.GroupRepository;
import app.repository.GroupMemberRepository;
import app.repository.GoalRepository;
import app.repository.UserRepository;
import java.util.List;

public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public GroupService(GroupRepository groupRepository,
                        GroupMemberRepository groupMemberRepository,
                        GoalRepository goalRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    public Group createGroup(String name, String description, int creatorId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название группы не может быть пустым");
        }

        Group group = new Group(name.trim(), description, creatorId);
        Group savedGroup = groupRepository.save(group);

        GroupMember creatorMember = new GroupMember(creatorId, savedGroup.getId(), "creator");
        groupMemberRepository.save(creatorMember);

        return savedGroup;
    }

    public void addMemberToGroup(int groupId, int userId, String role) {
        if (!groupMemberRepository.existsByUserAndGroup(userId, groupId)) {
            GroupMember member = new GroupMember(userId, groupId, role);
            groupMemberRepository.save(member);
        }
    }

    public void addMemberByEmail(int groupId, String email, String role) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с email " + email + " не найден");
        }

        if (groupMemberRepository.existsByUserAndGroup(user.getId(), groupId)) {
            throw new IllegalArgumentException("Пользователь уже является участником группы");
        }

        addMemberToGroup(groupId, user.getId(), role);
    }

    public void removeMemberFromGroup(int groupId, int userId) {
        GroupMember member = groupMemberRepository.findByUserAndGroup(userId, groupId);
        if (member != null) {
            if (member.isCreator()) {
                transferOwnership(groupId, userId);
            } else {
                groupMemberRepository.delete(member.getId());
            }
        }
    }

    public boolean deleteGroup(int groupId) {
        Group group = groupRepository.findById(groupId);
        if (group == null) {
            return false;
        }

        goalRepository.deleteByGroupId(groupId);

        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        for (GroupMember member : members) {
            groupMemberRepository.delete(member.getId());
        }

        return groupRepository.delete(groupId);
    }

    public List<Group> getUserGroups(int userId) {
        return groupMemberRepository.findByUserId(userId).stream()
                .map(member -> groupRepository.findById(member.getGroupId()))
                .filter(group -> group != null)
                .toList();
    }

    public List<GroupMember> getGroupMembers(int groupId) {
        return groupMemberRepository.findByGroupId(groupId);
    }

    public Goal createGroupGoal(int groupId, String name, double targetAmount) {
        Goal goal = new Goal(groupId, name, targetAmount);
        return goalRepository.save(goal);
    }

    private void transferOwnership(int groupId, int currentOwnerId) {
        GroupMember newOwner = groupMemberRepository.getOldestMemberAfterCreator(groupId);

        if (newOwner != null) {
            groupMemberRepository.updateRole(newOwner.getUserId(), groupId, "creator");
            groupMemberRepository.deleteByUserAndGroup(currentOwnerId, groupId);
        } else {
            deleteGroup(groupId);
        }
    }

    public boolean isUserMemberOfGroup(int userId, int groupId) {
        return groupMemberRepository.existsByUserAndGroup(userId, groupId);
    }

    public boolean isUserCreatorOfGroup(int userId, int groupId) {
        GroupMember member = groupMemberRepository.findByUserAndGroup(userId, groupId);
        return member != null && member.isCreator();
    }

    public int getGroupMemberCount(int groupId) {
        return groupMemberRepository.countByGroupId(groupId);
    }

    public Group getGroupById(int groupId) {
        return groupRepository.findById(groupId);
    }
}