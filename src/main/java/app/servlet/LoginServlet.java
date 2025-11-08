package app.servlet;

import app.exception.AuthenticationException;
import app.service.SecurityService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.securityService = (SecurityService) config.getServletContext().getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String sessionId;
        try {
            sessionId = securityService.loginUser(email, password);
        } catch (AuthenticationException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        Cookie cookie = new Cookie("sessionId", sessionId);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        resp.addCookie(cookie);

        String originalUrl = (String) req.getSession().getAttribute("originalUrl");
        if (originalUrl != null) {
            resp.sendRedirect(originalUrl);
            req.getSession().removeAttribute("originalUrl");
        } else {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }
}