package com.epam.jwd.cafe.pool;

import com.epam.jwd.cafe.config.DatabaseConfig;
import com.epam.jwd.cafe.exception.ApplicationStartException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
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

    public void initConnectionPool() {
        int initSize = DATABASE_CONFIG.getInitPoolSize();
        connections = ConnectionQueue.getInstance(initSize);
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

    public Connection retrieveConnection() {
        try {
            return connections.take();
        } catch (InterruptedException e) {
//            log.error("Waiting for a connection was interrupted");
            throw new RuntimeException();
        }
    }

    public void releaseConnection(final Connection connection) throws SQLException, InterruptedException {
        if (connection instanceof ProxyConnection) {
            connection.setAutoCommit(true);
            connections.removeConnectionFromTaken(connection);
            connections.put(connection);
        } else {
//            log.warn("Connection is fake");
        }
    }

    public void destroyConnectionPool(boolean withDeregistration) throws SQLException {
        connections.destroyConnections(withDeregistration);
    }

}