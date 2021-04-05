package com.epam.jwd.cafe.pool;

import com.epam.jwd.cafe.config.DatabaseConfig;
import com.epam.jwd.cafe.exception.ApplicationStartException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class provide pool of connections to database
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ConnectionPool {
    private final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private ConnectionQueue connections;
    private static final DatabaseConfig DATABASE_CONFIG = DatabaseConfig.getInstance();
    private static final AtomicBoolean IS_INSTANCE_CREATED = new AtomicBoolean(false);
    private static final Lock LOCK = new ReentrantLock();

    private ConnectionPool() {
    }

    public static ConnectionPool getInstance() {
        if (!IS_INSTANCE_CREATED.get()) {
            LOCK.lock();
            try {
                if (!IS_INSTANCE_CREATED.get()) {
                    instance = new ConnectionPool();
                    IS_INSTANCE_CREATED.set(true);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    /**
     * Fills connection pool
     *
     * @throws ClassNotFoundException if mysql jdbc Driver class not found
     */
    public void initConnectionPool() throws ClassNotFoundException {
        int initSize = DATABASE_CONFIG.getInitPoolSize();
        connections = ConnectionQueue.getInstance(initSize);
        Class.forName("com.mysql.cj.jdbc.Driver");
        try {
            for (int i = 0; i < initSize; i++) {
                connections.put(new ProxyConnection(DriverManager.getConnection(
                        DATABASE_CONFIG.retrieveDatabaseURL(), DATABASE_CONFIG.getLogin(), DATABASE_CONFIG.getPassword()))
                );
            }
        } catch (SQLException | InterruptedException e ) {
            throw new ApplicationStartException("Failed to initialize database connection pool");
        }
    }

    /**
     * Retrieve connection from connection pool
     * @throws RuntimeException when waiting for a connection was interrupted
     * @return {@link ProxyConnection}
     */
    public Connection retrieveConnection() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
            LOGGER.error("Waiting for a connection was interrupted");
            throw new RuntimeException();
        }
    }

    /**
     * Release the given connection to pool.
     * @param connection {@link ProxyConnection} instance
     */
    public void releaseConnection(final Connection connection) throws SQLException, InterruptedException {
        if (connection instanceof ProxyConnection) {
            connection.setAutoCommit(true);
            connections.removeConnectionFromTaken(connection);
            connections.put(connection);
        } else {
            LOGGER.warn("Connection is fake");
        }
    }
    /**
     * Destroy connection pool
     * @param withDeregistration true, if deregister drivers
     */
    public void destroyConnectionPool(boolean withDeregistration) throws SQLException {
        connections.destroyConnections(withDeregistration);
    }
}