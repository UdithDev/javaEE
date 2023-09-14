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


        try (Connection connection = pool.getConnection();){
            PreparedStatement preparedStatement = connection.prepareStatement("Select  *  from item");
            ResultSet resultSet = preparedStatement.executeQuery();

            JsonArrayBuilder allItems = Json.createArrayBuilder();
            while (resultSet.next()) {
                String itemID = resultSet.getString(1);
                String itemName = resultSet.getString(2);
                String itemQty = resultSet.getString(3);
                String itemPrice = resultSet.getString(4);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("code",itemID);
                objectBuilder.add("description",itemName);
                objectBuilder.add("itemQty",itemQty);
                objectBuilder.add("unitPrice",itemPrice);

                System.out.println(itemID+" "+itemName+" "+itemQty+" "+itemPrice);

                allItems.add(objectBuilder.build());

                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status", 200);
                response.add("message", "Done");
                response.add("data", allItems.build());

                writer.print(response.build());
            }
        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.setStatus(200);
            response.add("status", 400);
            response.add("message", "SQL ERROR");
            response.add("data", e.getLocalizedMessage());

            writer.print(response.build());
        }


    }
}
