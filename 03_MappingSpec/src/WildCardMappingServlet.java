import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//wild card mapping
@WebServlet(urlPatterns = "/items/*")
public class WildCardMappingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String servletPath = req.getServletPath();
        String method = req.getMethod();
        String pathInfo = req.getPathInfo();
        String contextPath = req.getContextPath();


        System.out.println("Servlet Path : "+servletPath);
        System.out.println("method : "+method);
        System.out.println("path Info : "+pathInfo);
        System.out.println("context Path : "+contextPath);



        System.out.println("Wild card mapping spec invoked");
    }
}
