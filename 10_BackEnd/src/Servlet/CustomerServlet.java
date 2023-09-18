package Servlet;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DO Get Method INVOKED");

        /*resp.addHeader("Access-Control-Allow-Origin", "*");*/
        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");

        PrintWriter writer = resp.getWriter();
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM  customer");
            ResultSet resultSet = preparedStatement.executeQuery();

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                String salary = resultSet.getString(4);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("id", id);
                objectBuilder.add("name", name);
                objectBuilder.add("address", address);
                objectBuilder.add("salary", salary);

                arrayBuilder.add(objectBuilder.build());
            }
            connection.close();


            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 200);
            response.add("message", "Done");
            response.add("data", arrayBuilder.build());

            writer.print(response.build());
        } catch (SQLException e) {
            e.printStackTrace();

            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.setStatus(200);
            response.add("status", 400);
            response.add("message", "SQL Error");
            response.add("data", e.getLocalizedMessage());

            writer.print(response.build());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*resp.addHeader("Access-Control-Allow-Origin", "*");*/
        System.out.println("Post method invoked");

        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");
        PrintWriter writer = resp.getWriter();


        try (Connection connection = pool.getConnection()) {

            String cusID = req.getParameter("cusID");
            String name = req.getParameter("cusName");
            String address = req.getParameter("cusAddress");
            String salary = req.getParameter("cusSalary");

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO customer values (?,?,?,?)");
            pstm.setObject(1, cusID);
            pstm.setObject(2, name);
            pstm.setObject(3, address);
            pstm.setObject(4, salary);
            int rst = pstm.executeUpdate();

            resp.setContentType("application/json");
            if (rst > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
//                resp.setStatus(HttpServletResponse.SC_CREATED);//201
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Added");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Unsuccessful");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Delete Method Invoked");
        /*resp.addHeader("Access-Control-Allow-Origin", "*");*/
        String cusID = req.getParameter("cusID");
        System.out.println(cusID);
        resp.setContentType("application/json");

        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");
        PrintWriter writer = resp.getWriter();
        try (Connection connection = pool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE from customer where cusId=?");
            preparedStatement.setObject(1, cusID);
            if (preparedStatement.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Deleted !!!!!");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Wrong Id Entered");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(200);
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*resp.addHeader("Access-Control-Allow-Origin", "*");*/
        System.out.println("Update");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        String address = jsonObject.getString("address");
        String salary = jsonObject.getString("salary");
        System.out.println(id + " " + name + " " + address + " " + salary);

        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");


        PrintWriter writer = resp.getWriter();
        try (Connection connection = pool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer SET cusName =? ,cusAddress=?, cusSalary=?  WHERE cusId=?");
            preparedStatement.setObject(4, id);
            preparedStatement.setObject(1, name);
            preparedStatement.setObject(2, address);
            preparedStatement.setObject(3, salary);

            resp.setContentType("application/json");
            if ( preparedStatement.executeUpdate() >0){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
               /* objectBuilder.add("data", objectBuilder.build());
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Updated Successful");
*/
                objectBuilder.add("data",objectBuilder.build());
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Update Successful");

                writer.print(objectBuilder.build());
            } else{
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Update Failed");
                objectBuilder.add("data", objectBuilder.build());
                writer.print(objectBuilder.build());
            }

        } catch (SQLException e) {
            e.printStackTrace();

            resp.setStatus(HttpServletResponse.SC_OK);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("data", e.getLocalizedMessage());
            objectBuilder.add("message", "Error");
            objectBuilder.add("status", 500);
            writer.print(objectBuilder.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*resp.addHeader("Access-Control-Allow-Origin", "*");*/

    }
}
