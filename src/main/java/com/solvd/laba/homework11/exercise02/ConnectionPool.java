package com.solvd.laba.homework11.exercise02;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnection.class.getName());
    private static volatile ConnectionPool instance;
    private static final int POOL_SIZE = 5;
    private static final int SLEEP_BETWEEN_CHECKS_MILISECONDS = 200;

    private volatile List<DatabaseConnection> connections;
    private volatile List<DatabaseConnection> usedConnections;


    private ConnectionPool(int poolSize) {
        // create pool and fill it with uninitialized objects
        this.connections = new CopyOnWriteArrayList<>(Collections.nCopies(poolSize, null));
        this.usedConnections = new CopyOnWriteArrayList<>();
    }

    public static synchronized ConnectionPool getInstance() {
        if (ConnectionPool.instance == null) {
            LOGGER.info("Creating instance of ConnectionPool");
            ConnectionPool.instance = new ConnectionPool(POOL_SIZE);
        }
        return ConnectionPool.instance;
    }

    public synchronized DatabaseConnection getConnection() {
        // wait for avaliable connection
        while (!connectionsAvaliable()) {
            try {
                Thread.sleep(SLEEP_BETWEEN_CHECKS_MILISECONDS);
            } catch (InterruptedException e) {
                // not important here
            }
        }

        // try to find opened connection
        DatabaseConnection connection = null;
        for (DatabaseConnection c : this.connections) {
            if (c == null || this.usedConnections.contains(c)) {
                continue;
            }
            connection = c;
            break;
        }

        // if opened connection not found, open one
        if (connection == null) {
            for (int i = 0; i < this.connections.size(); i++) {
                DatabaseConnection c = this.connections.get(i);
                if (c != null && this.usedConnections.contains(c)) {
                    continue;
                }
                connection = new DatabaseConnection();
                this.connections.set(i, connection);
                break;
            }
        }

        this.usedConnections.add(connection);
        return connection;
    }

    public void releaseConnection(DatabaseConnection connection) {
        this.usedConnections.remove(connection);
    }

    protected synchronized boolean connectionsAvaliable() {
        return this.usedConnections.size() < this.connections.size();
    }
}
