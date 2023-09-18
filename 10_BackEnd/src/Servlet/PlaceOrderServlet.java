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

@WebServlet(urlPatterns = "/purchase_order")
public class PlaceOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get method invoked");

        String orderID = req.getParameter("oid");


        ServletContext servletContext = req.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");

        PrintWriter writer = resp.getWriter();
        try (Connection connection = pool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select Orders.orderID,Orders.date,Orders.cusId,order_items.itemID,order_items.qty,order_items.itemPrice from Orders inner join order_items on Orders.orderID = order_items.orderID where Orders.orderID=?");
            preparedStatement.setObject(1, orderID);

            ResultSet rst = preparedStatement.executeQuery();

            JsonArrayBuilder allOrders = Json.createArrayBuilder();

            while (rst.next()) {
                String oid = rst.getString(1);
                String date = rst.getString(2);
                String cusId = rst.getString(3);
                String itemCode = rst.getString(4);
                String qty = rst.getString(5);
                String unitPrice = rst.getString(6);


                JsonObjectBuilder orders = Json.createObjectBuilder();
                orders.add("oid", oid);
                orders.add("date", date);
                orders.add("customerID", cusId);
                orders.add("itemCode", itemCode);
                orders.add("qty", qty);
                orders.add("unitPrice", unitPrice);

                allOrders.add(orders.build());
            }

            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 200);
            response.add("message", "Loaded");
            response.add("data", allOrders.build());

            writer.print(response.build());
        } catch (SQLException e) {
            e.printStackTrace();

            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 500);
            response.add("message", "Error");
            response.add("data", e.getLocalizedMessage());
        }

    }
}
