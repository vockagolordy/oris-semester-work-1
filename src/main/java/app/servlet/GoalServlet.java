package app.servlet;

import app.model.Goal;
import app.model.User;
import app.service.GoalService;
import app.service.GroupService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/goals/*")
public class GoalServlet extends HttpServlet {
    private GoalService goalService;
    private GroupService groupService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.goalService = (GoalService) config.getServletContext().getAttribute("goalService");
        this.groupService = (GroupService) config.getServletContext().getAttribute("groupService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("currentUser");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Goal> personalGoals = goalService.getPersonalGoals(user.getId());
            List<Goal> groupGoals = goalService.getGroupGoals(user.getId());

            req.setAttribute("personalGoals", personalGoals);
            req.setAttribute("groupGoals", groupGoals);
            req.getRequestDispatcher("/WEB-INF/views/goals/goals.jsp").forward(req, resp);
        } else if (pathInfo.equals("/add")) {
            req.setAttribute("userGroups", groupService.getUserGroups(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/goals/add-goal.jsp").forward(req, resp);
        } else if (pathInfo.startsWith("/contribute/")) {
            try {
                int goalId = Integer.parseInt(pathInfo.substring(12));
                Goal goal = goalService.getGoalById(goalId);
                if (goal != null) {
                    req.setAttribute("goal", goal);
                    req.getRequestDispatcher("/WEB-INF/views/goals/contribute.jsp").forward(req, resp);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
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
            double targetAmount = Double.parseDouble(req.getParameter("targetAmount"));
            String goalType = req.getParameter("goalType");

            if ("personal".equals(goalType)) {
                goalService.createPersonalGoal(user.getId(), name, targetAmount);
            } else if ("group".equals(goalType)) {
                int groupId = Integer.parseInt(req.getParameter("groupId"));
                goalService.createGroupGoal(groupId, name, targetAmount);
            }
            resp.sendRedirect(req.getContextPath() + "/goals");
        } else if ("contribute".equals(action)) {
            int goalId = Integer.parseInt(req.getParameter("goalId"));
            double amount = Double.parseDouble(req.getParameter("amount"));

            goalService.contributeToGoal(goalId, user.getId(), amount);
            resp.sendRedirect(req.getContextPath() + "/goals");
        } else if ("createGroupGoal".equals(action)) {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            String name = req.getParameter("name");
            double targetAmount = Double.parseDouble(req.getParameter("targetAmount"));

            if (groupService.isUserCreatorOfGroup(user.getId(), groupId)) {
                goalService.createGroupGoal(groupId, name, targetAmount);
                req.getSession().setAttribute("message", "Цель группы создана");
            }
            resp.sendRedirect(req.getContextPath() + "/groups/" + groupId);

        } else if ("updateGroupGoal".equals(action)) {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            String name = req.getParameter("name");
            double targetAmount = Double.parseDouble(req.getParameter("targetAmount"));

            if (groupService.isUserCreatorOfGroup(user.getId(), groupId)) {
                goalService.updateGroupGoal(groupId, name, targetAmount);
                req.getSession().setAttribute("message", "Цель группы обновлена");
            }
            resp.sendRedirect(req.getContextPath() + "/groups/" + groupId);
        }
    }
}