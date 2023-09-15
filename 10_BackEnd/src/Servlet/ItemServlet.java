package Servlet;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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
        resp.addHeader("Access-Control-Allow-Origin", "*");

        PrintWriter writer = resp.getWriter();

        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");


        try{
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
        resp.addHeader("Access-Control-Allow-Origin", "*");

        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");
        PrintWriter writer = resp.getWriter();
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        String itemQty = req.getParameter("itemQty");
        String unitPrice = req.getParameter("unitPrice");

        System.out.println(code+" "+description+" "+itemQty+" "+unitPrice);

        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("Insert INTO item values (?,?,?,?)");

            preparedStatement.setObject(1,code);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,itemQty);
            preparedStatement.setObject(4,unitPrice);

            resp.setContentType("application/json");

            if(preparedStatement.executeUpdate()>0){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message","Added Successful");
                objectBuilder.add("data"," ");
                writer.print(objectBuilder.build());
            }
            else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("status", 400);
                objectBuilder.add("message","Data Add Failed");
                objectBuilder.add("data"," ");
                writer.print(objectBuilder.build());
            }

        } catch ( SQLException e){
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
        System.out.println(code);
    }
}
