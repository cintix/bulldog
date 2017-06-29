package dk.tv2.bulldog.backend.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author migo
 */
public class CORSResponseFilter implements Filter {

    /**
     * the init method
     * 
     * @param filterConfig
     * @throws ServletException 
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     *
     * @param servletRequest
     * @param servletResponse
     * @param chain
     * @throws IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia, origin, content-type, accept, authorization");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(request, servletResponse);
    }

    /**
     * Destory
     */
    @Override
    public void destroy() {
    }

}