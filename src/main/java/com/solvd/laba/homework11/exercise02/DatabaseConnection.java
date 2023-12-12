package com.solvd.laba.homework11.exercise02;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseConnection {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseConnection.class.getName());
    private boolean isOpened;

    public DatabaseConnection() {
        this.isOpened = true;
        LOGGER.info("connection with database established");
    }

    public boolean isClosed() {
        return !this.isOpened;
    }

    public void close() {
        this.isOpened = false;
        LOGGER.info("connection closed");
    }

    public String queryDatabase(String query) {
        if (!this.isOpened) {
            throw new ClosedConnectionException("connection is closed");
        }
        LOGGER.info("query successfully executed: %s".formatted(query));
        return "some result";
    }
}
