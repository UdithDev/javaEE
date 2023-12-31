package Listeners;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("context Initialized");

        BasicDataSource pool=new BasicDataSource();
        pool.setDriverClassName("com.mysql.jdbc.Driver");
        pool.setUrl("jdbc:mysql://localhost:3306/pos_system");
        pool.setUsername("root");
        pool.setPassword("1234");
        pool.setMaxTotal(5);
        pool.setInitialSize(5);

        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute("pool",pool);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
