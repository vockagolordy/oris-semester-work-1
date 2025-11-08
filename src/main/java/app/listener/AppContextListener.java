package app.listener;

import app.repository.*;
import app.service.*;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        UserRepository userRepository = new UserRepositoryJdbc();
        SessionRepository sessionRepository = new SessionRepositoryJdbc();
        GroupRepository groupRepository = new GroupRepositoryJdbc();
        GroupMemberRepository groupMemberRepository = new GroupMemberRepositoryJdbc();
        GoalRepository goalRepository = new GoalRepositoryJdbc();
        ContributionRepository contributionRepository = new ContributionRepositoryJdbc();
        ExpenseRepository expenseRepository = new ExpenseRepositoryJdbc();

        SecurityService securityService = new SecurityServiceImpl(userRepository, sessionRepository);
        UserService userService = new UserService(userRepository);
        GroupService groupService = new GroupService(groupRepository, groupMemberRepository, goalRepository, userRepository);
        GoalService goalService = new GoalService(goalRepository, contributionRepository, groupMemberRepository);
        ContributionService contributionService = new ContributionService(contributionRepository);
        ExpenseService expenseService = new ExpenseService(expenseRepository);

        var ctx = sce.getServletContext();
        ctx.setAttribute("securityService", securityService);
        ctx.setAttribute("userService", userService);
        ctx.setAttribute("groupService", groupService);
        ctx.setAttribute("goalService", goalService);
        ctx.setAttribute("contributionService", contributionService);
        ctx.setAttribute("expenseService", expenseService);

        ctx.setAttribute("userRepository", userRepository);
        ctx.setAttribute("sessionRepository", sessionRepository);
        ctx.setAttribute("groupRepository", groupRepository);
        ctx.setAttribute("groupMemberRepository", groupMemberRepository);
        ctx.setAttribute("goalRepository", goalRepository);
        ctx.setAttribute("contributionRepository", contributionRepository);
        ctx.setAttribute("expenseRepository", expenseRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}