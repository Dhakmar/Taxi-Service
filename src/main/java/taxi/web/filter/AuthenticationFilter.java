package taxi.web.filter;

import taxi.lib.Inject;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {
    @Inject
    private Set<String> allowedUrls;

    @Override
    public void init(FilterConfig filterConfig) {
        allowedUrls = new HashSet<>();
        allowedUrls.add("/login");
        allowedUrls.add("/drivers/add");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Long driverId = (Long) session.getAttribute("driver_id");
        if (driverId == null && allowedUrls.contains(req.getServletPath())) {
            chain.doFilter(req, resp);
            return;
        }
        if (driverId == null) {
            resp.sendRedirect("/login");
            return;
        }
        chain.doFilter(req, resp);
    }
}