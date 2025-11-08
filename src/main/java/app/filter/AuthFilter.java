package app.filter;

import app.model.User;
import app.service.SecurityService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/expenses/*", "/goals/*", "/groups/*", "/dashboard"})
public class AuthFilter implements Filter {
    private SecurityService securityService;

    @Override
    public void init(FilterConfig config) {
        securityService = (SecurityService) config.getServletContext().getAttribute("securityService");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        User user = getUser(request);
        if (user == null) {
            String originalUrl = request.getRequestURI();
            if (request.getQueryString() != null) {
                originalUrl += "?" + request.getQueryString();
            }
            request.getSession().setAttribute("originalUrl", originalUrl);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("currentUser", user);
        chain.doFilter(req, resp);
    }

    private User getUser(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("sessionId".equals(c.getName())) {
                    try {
                        return securityService.getUser(c.getValue());
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}