package app.filter;

import app.model.Goal;
import app.model.User;
import app.service.GoalService;
import app.service.GroupService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class GoalAccessFilter implements Filter {
    private GoalService goalService;
    private GroupService groupService;

    @Override
    public void init(FilterConfig config) {
        goalService = (GoalService) config.getServletContext().getAttribute("goalService");
        groupService = (GroupService) config.getServletContext().getAttribute("groupService");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        User user = (User) request.getAttribute("currentUser");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String path = request.getServletPath();

        if (path.matches("/goals/\\d+(/.*)?")) {
            Integer goalId = extractGoalId(path);
            if (goalId != null && !hasGoalAccess(user.getId(), goalId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(req, resp);
    }

    private boolean hasGoalAccess(int userId, int goalId) {
        Goal goal = goalService.getGoalById(goalId);
        if (goal == null) {
            return false;
        }

        if (goal.isGroupGoal()) {
            return groupService.isUserMemberOfGroup(userId, goal.getGroupId());
        } else {
            return goal.getUserId() == userId;
        }
    }

    private Integer extractGoalId(String path) {
        try {
            String[] parts = path.split("/");
            return Integer.parseInt(parts[2]);
        } catch (Exception e) {
            return null;
        }
    }
}