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

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    public ItemServlet() {
        System.out.println("item servlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Item");


        PrintWriter writer = resp.getWriter();

        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");


        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM  item");
            ResultSet resultSet = preparedStatement.executeQuery();

            JsonArrayBuilder allItems = Json.createArrayBuilder();
            while (resultSet.next()) {
                String itemID = resultSet.getString(1);
                String itemName = resultSet.getString(2);
                String itemQty = resultSet.getString(3);
                String itemPrice = resultSet.getString(4);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("code", itemID);
                objectBuilder.add("description", itemName);
                objectBuilder.add("itemQty", itemQty);
                objectBuilder.add("unitPrice", itemPrice);


                allItems.add(objectBuilder.build());
            }
            connection.close();

            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 200);
            response.add("message", "Done");
            response.add("data", allItems.build());

            writer.print(response.build());


        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.setStatus(200);
            response.add("status", 400);
            response.add("message", "SQL ERROR");
            response.add("data", e.getLocalizedMessage());

            writer.print(response.build());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Post method Invoked");


        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");
        PrintWriter writer = resp.getWriter();
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        String itemQty = req.getParameter("itemQty");
        String unitPrice = req.getParameter("unitPrice");

        System.out.println(code + " " + description + " " + itemQty + " " + unitPrice);

        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Insert INTO item values (?,?,?,?)");

            preparedStatement.setObject(1, code);
            preparedStatement.setObject(2, description);
            preparedStatement.setObject(3, itemQty);
            preparedStatement.setObject(4, unitPrice);

            resp.setContentType("application/json");

            if (preparedStatement.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Added Successful");
                objectBuilder.add("data", " ");
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Data Add Failed");
                objectBuilder.add("data", " ");
                writer.print(objectBuilder.build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_OK);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());

        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Do Delete Method Invoked");


        String code = req.getParameter("code");
        resp.setContentType("application/json");
        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");
        PrintWriter writer = resp.getWriter();

        try (Connection connection = pool.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement("DELETE from  item where itemID=?");
            pstm.setObject(1, code);

            if (pstm.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Delete!!!");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(200);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Failed!!!");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }
        } catch (SQLException throwbel) {
            throwbel.printStackTrace();

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(200);
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Error");
            objectBuilder.add("data", throwbel.getLocalizedMessage());
            writer.print(objectBuilder.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String code = jsonObject.getString("code");
        String description = jsonObject.getString("description");
        String itemQty = jsonObject.getString("itemQty");
        String unitPrice = jsonObject.getString("unitPrice");

        PrintWriter writer = resp.getWriter();

        try (Connection connection = pool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE  item set itemName=?, itemQty=?, itemPrice=? where itemID=?");
            preparedStatement.setObject(4, code);
            preparedStatement.setObject(1, description);
            preparedStatement.setObject(2, itemQty);
            preparedStatement.setObject(3, unitPrice);

            resp.setContentType("application/json");
            if (preparedStatement.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Updated");
                objectBuilder.add("data", "");


                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(200);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", " Updated Failed");
                objectBuilder.add("data", objectBuilder.build());

                writer.print(objectBuilder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(200);
            objectBuilder.add("status", 500);
            objectBuilder.add("message", " Error");
            objectBuilder.add("data", e.getLocalizedMessage());

            writer.print(objectBuilder.build());
        }
    }
}
