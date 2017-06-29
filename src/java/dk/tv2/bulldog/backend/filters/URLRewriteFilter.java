/*
 */
package dk.tv2.bulldog.backend.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author migo
 */
public class URLRewriteFilter implements Filter {

    private String fromURL;
    private String toURL;
    private int minimumMacthCount;

    @Override
    public void init(FilterConfig config) throws ServletException {
        fromURL = config.getInitParameter("from-url");
        toURL = config.getInitParameter("to-url");
        minimumMacthCount = Integer.parseInt(config.getInitParameter("matchs"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String requestURI = servletRequest.getRequestURI();
        String applicationRoot = request.getServletContext().getContextPath();

        if (requestURI.contains(applicationRoot)) {
            requestURI = requestURI.substring(requestURI.indexOf(applicationRoot) + applicationRoot.length());
        }

        String matchFilter = fromURL;
        String redirection = toURL;
        
        if (requestURI.matches(matchFilter)) {
            Pattern pattern = Pattern.compile(matchFilter);
            Matcher matcher = pattern.matcher(requestURI);

            int variableCount = 0;
            while (matcher.find()) {
                if (matcher.groupCount() == (minimumMacthCount+1) || (minimumMacthCount == 0 && matcher.groupCount() == 0)) {
                    int indexCount = matcher.groupCount() + 1;
                    for (int index = 2 ; index < indexCount; index++) {
                        variableCount++;
                        String argument = matcher.group(index);
                        if (argument.endsWith("/")) {
                            argument = argument.substring(0, argument.length() - 1);
                        } else if (argument.contains(".")) {
                            chain.doFilter(request, response);
                            return;
                        }
                        redirection = redirection.replaceAll("\\$" + variableCount, argument);
                    }
                    request.setCharacterEncoding("UTF-8");
                    request.getRequestDispatcher(redirection).forward(request, response);
                    return;
                }
            }
        }
        // Pass request back down the filter chain
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        /* Called before the Filter instance is removed 
         from service by the web container*/
    }

}
