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

public class ConnectionQueue {
    private final Logger LOGGER = LogManager.getLogger(ConnectionQueue.class);
    public static ConnectionQueue instance;
    private final Queue<Connection> FREE_CONNECTIONS;
    private final Queue<Connection> TAKEN_CONNECTIONS;
    private static final AtomicBoolean IS_INSTANCE_CREATED = new AtomicBoolean(false);
    private static final Lock INSTANCE_LOCK = new ReentrantLock();
    private final Lock queueLock = new ReentrantLock();
    private final Condition notFull = queueLock.newCondition();
    private final Condition notEmpty = queueLock.newCondition();
    private final int numOfFreeConnections;

    private ConnectionQueue(int initPoolSize) {
        FREE_CONNECTIONS = new ArrayDeque<>(initPoolSize);
        TAKEN_CONNECTIONS = new ArrayDeque<>(initPoolSize);
        numOfFreeConnections = initPoolSize;
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


    public void put(Connection connection) throws InterruptedException {
        queueLock.lock();
        try {
            while (TAKEN_CONNECTIONS.size() == numOfFreeConnections) {
                notFull.await();
            }
            FREE_CONNECTIONS.add(connection);
            notEmpty.signal();
        } finally {
            queueLock.unlock();
        }
    }

    public Connection take() throws InterruptedException {
        queueLock.lock();
        try {
            while (FREE_CONNECTIONS.size() == 0) {
                notEmpty.await();
            }
            ProxyConnection connection = (ProxyConnection) FREE_CONNECTIONS.poll();
            TAKEN_CONNECTIONS.add(connection);
            notFull.signal();
            return connection;
        } finally {
            queueLock.unlock();
        }
    }

    public void removeConnectionFromTaken(Connection connection) {
        TAKEN_CONNECTIONS.remove(connection);
    }

    public void destroyConnections(boolean withDeregistration) throws SQLException {
        try {
            for (Connection connection : FREE_CONNECTIONS) {
                ((ProxyConnection) connection).closeConnection();
            }

            for (Connection connection : TAKEN_CONNECTIONS) {
                connection.commit();
                ((ProxyConnection) connection).closeConnection();
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
