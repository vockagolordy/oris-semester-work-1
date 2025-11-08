package app.servlet;

import app.model.Goal;
import app.model.Group;
import app.model.GroupMember;
import app.model.User;
import app.repository.GoalRepository;
import app.service.GroupService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/groups/*")
public class GroupServlet extends HttpServlet {
    private GroupService groupService;
    private GoalRepository goalRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.groupService = (GroupService) config.getServletContext().getAttribute("groupService");
        this.goalRepository = (GoalRepository) config.getServletContext().getAttribute("goalRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("currentUser");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Group> userGroups = groupService.getUserGroups(user.getId());
            req.setAttribute("groups", userGroups);
            req.getRequestDispatcher("/WEB-INF/views/groups/groups.jsp").forward(req, resp);
        } else if (pathInfo.equals("/add")) {
            req.getRequestDispatcher("/WEB-INF/views/groups/add-group.jsp").forward(req, resp);
        } else if (pathInfo.startsWith("/")) {
            try {
                int groupId = Integer.parseInt(pathInfo.substring(1));
                Group group = groupService.getGroupById(groupId);
                if (group != null && groupService.isUserMemberOfGroup(user.getId(), groupId)) {
                    List<GroupMember> members = groupService.getGroupMembers(groupId);
                    Goal groupGoal = goalRepository.findByGroupIdSingle(groupId);
                    req.setAttribute("groupGoal", groupGoal);
                    req.setAttribute("group", group);
                    req.setAttribute("members", members);
                    req.setAttribute("isCreator", groupService.isUserCreatorOfGroup(user.getId(), groupId));
                    req.setAttribute("memberCount", groupService.getGroupMemberCount(groupId));
                    req.getRequestDispatcher("/WEB-INF/views/groups/group-details.jsp").forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("currentUser");
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String name = req.getParameter("name");
            String description = req.getParameter("description");

            if (name == null || name.trim().isEmpty()) {
                req.getSession().setAttribute("error", "Название группы не может быть пустым");
                resp.sendRedirect(req.getContextPath() + "/groups/add");
                return;
            }

            try {
                Group group = groupService.createGroup(name.trim(), description, user.getId());
                req.getSession().setAttribute("message", "Группа '" + group.getName() + "' успешно создана");
                resp.sendRedirect(req.getContextPath() + "/groups");
            } catch (Exception e) {
                req.getSession().setAttribute("error", "Ошибка при создании группы: " + e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/groups/add");
            }

        } else if ("addMember".equals(action)) {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            String memberEmail = req.getParameter("memberEmail");

            if (memberEmail == null || memberEmail.trim().isEmpty()) {
                req.getSession().setAttribute("error", "Email не может быть пустым");
                resp.sendRedirect(req.getContextPath() + "/groups/" + groupId);
                return;
            }

            if (groupService.isUserCreatorOfGroup(user.getId(), groupId)) {
                try {
                    groupService.addMemberByEmail(groupId, memberEmail.trim(), "member");
                    req.getSession().setAttribute("message", "Участник успешно добавлен");
                } catch (IllegalArgumentException e) {
                    req.getSession().setAttribute("error", e.getMessage());
                }
            } else {
                req.getSession().setAttribute("error", "Только создатель группы может добавлять участников");
            }
            resp.sendRedirect(req.getContextPath() + "/groups/" + groupId);

        } else if ("leave".equals(action)) {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            Group group = groupService.getGroupById(groupId);

            if (group != null) {
                groupService.removeMemberFromGroup(groupId, user.getId());
                req.getSession().setAttribute("message", "Вы вышли из группы '" + group.getName() + "'");
            }
            resp.sendRedirect(req.getContextPath() + "/groups");

        } else if ("delete".equals(action)) {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            Group group = groupService.getGroupById(groupId);

            if (group != null && groupService.isUserCreatorOfGroup(user.getId(), groupId)) {
                groupService.deleteGroup(groupId);
                req.getSession().setAttribute("message", "Группа '" + group.getName() + "' удалена");
            } else {
                req.getSession().setAttribute("error", "Только создатель группы может удалить её");
            }
            resp.sendRedirect(req.getContextPath() + "/groups");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("currentUser");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                int groupId = Integer.parseInt(pathInfo.substring(1));
                if (groupService.isUserCreatorOfGroup(user.getId(), groupId)) {
                    if (groupService.deleteGroup(groupId)) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}