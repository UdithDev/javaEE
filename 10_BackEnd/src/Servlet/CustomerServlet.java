package Servlet;

import org.apache.commons.dbcp2.BasicDataSource;
import util.ResponseUtil;

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
        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");

        PrintWriter writer = resp.getWriter();
        try (Connection connection = pool.getConnection()) {

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

            resp.getWriter().print(ResponseUtil.genJson("Success", "Loaded", arrayBuilder.build()));
        } catch (SQLException e) {
            e.printStackTrace();

            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
                resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));
            } else {
                resp.setStatus(200);
                resp.getWriter().print(ResponseUtil.genJson("Error", "Unsuccessful"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Delete Method Invoked");

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
                resp.getWriter().print(ResponseUtil.genJson("Success", "Customer Deleted..!"));
            } else {
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Customer Deleted Failed..!"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
            if (preparedStatement.executeUpdate() > 0) {

                resp.getWriter().print(ResponseUtil.genJson("Success", "Customer Updated ..!"));
            } else {
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Customer Updated Failed ..!"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }


}
