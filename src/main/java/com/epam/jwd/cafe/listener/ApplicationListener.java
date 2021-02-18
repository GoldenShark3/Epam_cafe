package com.epam.jwd.cafe.listener;

import com.epam.jwd.cafe.pool.ConnectionPool;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class ApplicationListener implements ServletContextListener {
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        connectionPool = ConnectionPool.getInstance();
        connectionPool.initConnectionPool();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            connectionPool.destroyConnectionPool(true);
        } catch (SQLException e) {
            //todo: log.error("Failed to destroy connection pool")
        }
    }
}
