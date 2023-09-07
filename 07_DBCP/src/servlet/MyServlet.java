package servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/hello")
public class MyServlet extends HttpServlet {

    public MyServlet() {
        System.out.println("Create object is First");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("Starting servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Do GET Method Invoked");
    }

    @Override
    public void destroy() {
        System.out.println("Servlet Dead");
    }
}
