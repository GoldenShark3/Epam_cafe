package com.epam.jwd.cafe.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class provide storing connections in {@link ConnectionPool}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ConnectionQueue {
    private final Logger LOGGER = LogManager.getLogger(ConnectionQueue.class);
    private static final AtomicBoolean IS_INSTANCE_CREATED = new AtomicBoolean(false);
    public static ConnectionQueue instance;
    private final Queue<Connection> FREE_CONNECTIONS;
    private final Queue<Connection> TAKEN_CONNECTIONS;
    private static final Lock INSTANCE_LOCK = new ReentrantLock();
    private final Lock queueLock = new ReentrantLock();
    private final Condition takenConnectionsIsEmpty = queueLock.newCondition();
    private final Condition freeConnectionsIsEmpty = queueLock.newCondition();
    private final int numOfConnections;

    private ConnectionQueue(int initPoolSize) {
        FREE_CONNECTIONS = new ArrayDeque<>(initPoolSize);
        TAKEN_CONNECTIONS = new ArrayDeque<>(initPoolSize);
        numOfConnections = initPoolSize;
    }

    public static ConnectionQueue getInstance(int initPoolSize) {
        if (!IS_INSTANCE_CREATED.get()) {
            INSTANCE_LOCK.lock();
            try {
                if (!IS_INSTANCE_CREATED.get()) {
                    instance = new ConnectionQueue(initPoolSize);
                    IS_INSTANCE_CREATED.set(true);
                }
            } finally {
                INSTANCE_LOCK.unlock();
            }
        }
        return instance;
    }


    /**
     * Put connection to {@link Queue FREE_CONNECTIONS}
     *
     * @param connection {@link ProxyConnection}
     */
    public void put(Connection connection) throws InterruptedException {
        queueLock.lock();
        try {
            while (FREE_CONNECTIONS.size() == numOfConnections) {
                takenConnectionsIsEmpty.await();
            }
            FREE_CONNECTIONS.add(connection);
            freeConnectionsIsEmpty.signal();
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Retrieve connection from {@link Queue FREE_CONNECTIONS}
     *
     * @return {@link ProxyConnection} instance
     * @throws InterruptedException if the current thread is interrupted
     */
    public Connection take() throws InterruptedException {
        queueLock.lock();
        try {
            while (TAKEN_CONNECTIONS.size() == numOfConnections) {
                freeConnectionsIsEmpty.await();
            }
            ProxyConnection connection = (ProxyConnection) FREE_CONNECTIONS.poll();
            TAKEN_CONNECTIONS.add(connection);
            takenConnectionsIsEmpty.signal();
            return connection;
        } finally {
            queueLock.unlock();
        }
    }

    /**
     * Remove connection from {@link Queue TAKEN_CONNECTIONS}
     *
     * @param connection {@link ProxyConnection}
     */
    protected void removeConnectionFromTaken(Connection connection) {
        TAKEN_CONNECTIONS.remove(connection);
    }

    /**
     * Destroy all connections in {@link ConnectionPool}
     *
     * @param withDeregistration true, if deregister drivers
     * @throws SQLException when closing connection was failed
     */
    public void destroyConnections(boolean withDeregistration) throws SQLException {
        try {
            for (Connection connection : FREE_CONNECTIONS) {
                ((ProxyConnection) connection).closeConnection();
                FREE_CONNECTIONS.remove();
            }

            for (Connection connection : TAKEN_CONNECTIONS) {
                connection.commit();
                ((ProxyConnection) connection).closeConnection();
                TAKEN_CONNECTIONS.remove();
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to close a connection. " + e);
        }
        if (withDeregistration) {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                DriverManager.deregisterDriver(drivers.nextElement());
            }
        }
    }
}
