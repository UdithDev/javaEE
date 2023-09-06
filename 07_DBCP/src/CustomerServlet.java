import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BasicDataSource pool=new BasicDataSource();
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setUsername("root");
        pool.setPassword("1234");
        pool.setUrl("jdbc:mysql://localhost:3306/pos_system");
        pool.setMaxTotal(5);
        pool.setInitialSize(5);

        ServletContext servletContext = req.getServletContext();
        servletContext.setAttribute("pool",pool);

        try {
            Connection connection = pool.getConnection();
            PreparedStatement pstm = connection.prepareStatement("select * from  customer");
            ResultSet rst = pstm.executeQuery();
           while ( rst.next()){
               String id = rst.getString(1);
               System.out.println(id);
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
