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
        try {
            connectionPool.initConnectionPool();
        } catch (ClassNotFoundException e) {
            //todo: log.error("failed to init connection pool", e);
        }

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
