package app.filter;

import app.model.User;
import app.service.GroupService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class GroupAccessFilter implements Filter {
    private GroupService groupService;

    @Override
    public void init(FilterConfig config) {
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

        if (path.matches("/groups/\\d+(/.*)?")) {
            Integer groupId = extractGroupId(path);
            if (groupId != null && !groupService.isUserMemberOfGroup(user.getId(), groupId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(req, resp);
    }

    private Integer extractGroupId(String path) {
        try {
            String[] parts = path.split("/");
            return Integer.parseInt(parts[2]);
        } catch (Exception e) {
            return null;
        }
    }
}