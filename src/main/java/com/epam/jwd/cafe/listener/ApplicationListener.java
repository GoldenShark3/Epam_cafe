package com.epam.jwd.cafe.listener;

import com.epam.jwd.cafe.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;


/**
 * The class provide initialization {@link ConnectionPool} on application start up
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
@WebListener
public class ApplicationListener implements ServletContextListener {
    private final Logger LOGGER = LogManager.getLogger(ApplicationListener.class);
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        connectionPool = ConnectionPool.getInstance();
        try {
            connectionPool.initConnectionPool();
        } catch (ClassNotFoundException e) {
            LOGGER.error("failed to init connection pool", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            connectionPool.destroyConnectionPool(true);
        } catch (SQLException e) {
            LOGGER.error("Failed to destroy connection pool", e);
        }
    }
}
