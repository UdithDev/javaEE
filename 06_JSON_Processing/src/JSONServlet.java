import javax.json.*;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/json")
public class JSONServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

/*
 JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("id", "C001");
        objectBuilder.add("name", "Udith Dev");
        objectBuilder.add("address", "Kalutara");
        objectBuilder.add("salary", 1000);
        JsonObject build = objectBuilder.build();

        PrintWriter writer = resp.getWriter();
        writer.print(build);
*/


        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("id", "C001");
        objectBuilder.add("name", "Udith Dev");
        objectBuilder.add("address", "Kalutara");
        objectBuilder.add("salary", 1000);
        arrayBuilder.add(objectBuilder.build());

        PrintWriter writer = resp.getWriter();
        writer.print(arrayBuilder.build());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Someone Called DD PUT method");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");

        System.out.println(id + " " + name);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonArray jsonArray = reader.readArray();
        for (JsonValue jsonValue:jsonArray) {
            String id = jsonValue.asJsonObject().getString("id");
            String name = jsonValue.asJsonObject().getString("name");
            String address = jsonValue.asJsonObject().getString("address");
            String salary = jsonValue.asJsonObject().getString("salary");

            System.out.println(id+" "+name+" "+address+" "+salary);

        }
       /* try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos_system", "root", "1234");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customer values (?,?,?,?)");
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, name);
            preparedStatement.setObject(3, address);
            preparedStatement.setObject(4, salary);
            int rst = preparedStatement.executeUpdate();


            resp.setContentType("application/json");
            if (rst > 0) {
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);//201
                objectBuilder.add("data", "");
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Added !!!!!");
                arrayBuilder.add(objectBuilder.build());
                resp.getWriter().print(arrayBuilder.build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
*/
    }
}
