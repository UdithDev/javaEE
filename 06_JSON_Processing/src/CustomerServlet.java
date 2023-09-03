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
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            ResultSet rst = connection.prepareStatement("select * from customer").executeQuery();

            resp.addHeader("Content-Type", "application/json");

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String salary = rst.getString(4);
                JsonObjectBuilder customerObject = Json.createObjectBuilder();

                customerObject.add("id", id);
                customerObject.add("name", name);
                customerObject.add("address", address);
                customerObject.add("salary", salary);

                arrayBuilder.add(customerObject.build());
            }

            PrintWriter writer = resp.getWriter();


            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 200);
            response.add("message", "Done");
            response.add("data", arrayBuilder.build());

            writer.print(response.build());

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
            preparedStatement.setObject(3, cusAddress);
            preparedStatement.setObject(4, cusSalary);
            int rst = preparedStatement.executeUpdate();

            resp.setContentType("application/json");
            if (rst > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);//201
                objectBuilder.add("data", "");
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Added !!!!!");
                resp.getWriter().print(objectBuilder.build());
            }
        } catch (ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_OK);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 400);
            objectBuilder.add("message", "ERROR");
            objectBuilder.add("data", e.getLocalizedMessage());
            resp.getWriter().print(objectBuilder.build());

            e.printStackTrace();

        } catch (SQLException throwables) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 400);
            objectBuilder.add("message", "ERROR");
            objectBuilder.add("data", throwables.getLocalizedMessage());
            resp.getWriter().print(objectBuilder.build());

            resp.setStatus(HttpServletResponse.SC_OK);
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("delete");
        String customerID = req.getParameter("cusId");
        System.out.println(customerID);
        PrintWriter writer = resp.getWriter();
        resp.addHeader("Content-Type", "application/json");
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM customer WHERE cusId=?");
            preparedStatement.setObject(1, customerID);


            if (preparedStatement.executeUpdate() > 0) {

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("data", "");
                objectBuilder.add("message", "Successfully Deleted");
                objectBuilder.add("status", 200);
                writer.print(objectBuilder.build());
            } else{
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("data", "");
                objectBuilder.add("message", "Wrong Id Entered");
                objectBuilder.add("status", 400);
                writer.print(objectBuilder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
//            resp.sendError(500,e.getMessage());

            resp.setStatus(200);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("data", e.getLocalizedMessage());
            objectBuilder.add("message", "Error");
            objectBuilder.add("status", 500);
            writer.print(objectBuilder.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Update");
        String cusId = req.getParameter("customerId");
        String cusName = req.getParameter("customerName");
        String cusAddress = req.getParameter("customerAddress");
        String cusSalary = req.getParameter("customerSalary");

        System.out.println(cusId + " " + cusName + " " + cusAddress + " " + cusSalary);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer SET cusName=?,cusAddress=?,cusSalary=? WHERE cusId=?");
            preparedStatement.setObject(4, cusId);
            preparedStatement.setObject(1, cusName);
            preparedStatement.setObject(2, cusAddress);
            preparedStatement.setObject(3, cusSalary);

            resp.setContentType("application/json");

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            if (preparedStatement.executeUpdate() > 0) {
                objectBuilder.add("data", objectBuilder.build());
                objectBuilder.add("Status", 200);
                objectBuilder.add("message", "Updated");
                PrintWriter writer = resp.getWriter();
                writer.print(objectBuilder.build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
