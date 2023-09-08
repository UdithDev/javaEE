package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/customer")
public class MyFilter implements Filter {


    public MyFilter() {
        System.out.println("Object Created from MyFilter");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(" my filer initialized ");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter Method Invoked  ");
    }

    @Override
    public void destroy() {
        System.out.println("destroy Method Invoked  ");
    }
}
