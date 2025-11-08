package app.filter;

import app.model.User;
import app.service.SecurityService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class UserFilter implements Filter {
    private SecurityService securityService;

    @Override
    public void init(FilterConfig config) {
        securityService = (SecurityService) config.getServletContext().getAttribute("securityService");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        User user = getUserFromCookies(request);
        if (user != null) {
            request.setAttribute("currentUser", user);
        }

        chain.doFilter(req, resp);
    }

    private User getUserFromCookies(HttpServletRequest req) {
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