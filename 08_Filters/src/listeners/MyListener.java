package listeners;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Context Initialized");
        BasicDataSource pool=new BasicDataSource();
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setUsername("root");
        pool.setPassword("1234");
        pool.setUrl("jdbc:mysql://localhost:3306/pos_system");
        pool.setMaxTotal(5);
        pool.setInitialSize(5);
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("pool",pool);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context Destroy");

        ServletContext servletContext = servletContextEvent.getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("pool");
        try {
            pool.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
