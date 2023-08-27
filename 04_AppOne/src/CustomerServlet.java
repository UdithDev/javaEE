import com.mysql.cj.xdevapi.JsonArray;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

       String customerId = req.getParameter("customerId");
        String customerName = req.getParameter("customerName");
        String customerAddress = req.getParameter("customerAddress");
        String customerSalary = req.getParameter("customerSalary");

        System.out.println(customerId+" "+customerName+" "+customerAddress+" "+customerSalary);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            ResultSet rst = connection.prepareStatement("select * from customer").executeQuery();

            resp.addHeader("Content-Type", "application/json");
             String json="[";
            while (rst.next()) {
                String customer="{";
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String salary = rst.getString(4);
                customer+="\"id\":\""+id+"\",";
                customer+="\"name\":\""+name+"\",";
                customer+="\"address\":\""+address+"\",";
                customer+="\"salary\":\""+salary+"\"";
                customer+="},";
                json+=customer;
            }
            json=json+"]";

            resp.getWriter().print(json.substring(0,json.length()-2)+"]");


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String cusId = req.getParameter("customerId");
            String cusName = req.getParameter("customerName");
            String cusAddress = req.getParameter("customerAddress");
            String cusSalary = req.getParameter("customerSalary");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer values (?,?,?,?)");
            preparedStatement.setObject(1, cusId);
            preparedStatement.setObject(2, cusName);
            preparedStatement.setObject(3,cusAddress);
            preparedStatement.setObject(4,cusSalary);
            int rst = preparedStatement.executeUpdate();
            if(rst>0){
                PrintWriter writer = resp.getWriter();
                writer.write("customer Add");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}
