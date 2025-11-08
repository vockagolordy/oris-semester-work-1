package app.servlet;

import app.model.Expense;
import app.model.User;
import app.service.ExpenseService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/expenses/*")
public class ExpenseServlet extends HttpServlet {
    private ExpenseService expenseService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.expenseService = (ExpenseService) config.getServletContext().getAttribute("expenseService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("currentUser");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Expense> expenses = expenseService.getUserExpenses(user.getId());
            req.setAttribute("expenses", expenses);
            req.getRequestDispatcher("/WEB-INF/views/expenses/expenses.jsp").forward(req, resp);
        } else if (pathInfo.equals("/add")) {
            req.getRequestDispatcher("/WEB-INF/views/expenses/add-expense.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("currentUser");
        String type = req.getParameter("type");
        String category = req.getParameter("category");
        double amount = Double.parseDouble(req.getParameter("amount"));
        String description = req.getParameter("description");

        expenseService.addExpense(user.getId(), type, category, amount, description);
        resp.sendRedirect(req.getContextPath() + "/expenses");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                int expenseId = Integer.parseInt(pathInfo.substring(1));
                if (expenseService.deleteExpense(expenseId)) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
}