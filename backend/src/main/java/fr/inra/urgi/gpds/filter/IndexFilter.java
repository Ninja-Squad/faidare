package fr.inra.urgi.gpds.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * Filter that forwards all GET requests to non-static and non-api resources to index.html. This filter is necessary
 * to support deep-linking for URLs generated by the Angular router.
 * <p>
 * Adapted from data-discovery
 *
 * @author gcornut
 */
@Component
@WebFilter("/*")
public class IndexFilter implements Filter {

    private static final String[] API_PREFIXES = {
        "/brapi/v1", "/gnpis/v1", "/actuator", "/api-docs", "/v2/api-docs", "/swagger-resources"
    };

    private static final String[] STATIC_FILES = {
        "/index.html", "/swagger-ui.html"
    };

    private static final String[] STATIC_SUFFIXES = {
        ".js", ".css", ".ico", ".png", ".jpg", ".gif", ".eot", ".svg",
        ".woff2", ".ttf", ".woff"
    };

    @Override
    public void doFilter(
        ServletRequest req,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (mustForward(request)) {
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean mustForward(HttpServletRequest request) {
        if (!request.getMethod().equals("GET")) {
            return false;
        }

        String fullUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = fullUri.substring(contextPath.length());

        return !isApiOrStaticResource(uri);
    }

    private boolean isApiOrStaticResource(String uri) {
        // Starts with API prefix
        return Arrays.stream(API_PREFIXES).anyMatch(uri::startsWith)
            // or is static file
            || Arrays.asList(STATIC_FILES).contains(uri)
            // or has static file suffix
            || Arrays.stream(STATIC_SUFFIXES).anyMatch(uri::endsWith);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}