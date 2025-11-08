package app.servlet;

import app.model.User;
import app.service.ExpenseService;
import app.service.GoalService;
import app.service.GroupService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private ExpenseService expenseService;
    private GoalService goalService;
    private GroupService groupService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.expenseService = (ExpenseService) config.getServletContext().getAttribute("expenseService");
        this.goalService = (GoalService) config.getServletContext().getAttribute("goalService");
        this.groupService = (GroupService) config.getServletContext().getAttribute("groupService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("currentUser");

        double balance = expenseService.getUserBalance(user.getId());
        double totalIncome = expenseService.getTotalIncome(user.getId());
        double totalExpenses = expenseService.getTotalExpenses(user.getId());

        req.setAttribute("balance", balance);
        req.setAttribute("totalIncome", totalIncome);
        req.setAttribute("totalExpenses", totalExpenses);
        req.setAttribute("personalGoals", goalService.getPersonalGoals(user.getId()));
        req.setAttribute("userGroups", groupService.getUserGroups(user.getId()));

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}