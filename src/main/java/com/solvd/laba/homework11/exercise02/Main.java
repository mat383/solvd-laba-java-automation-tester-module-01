package com.solvd.laba.homework11.exercise02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static final int QUERY_COUNT = 7;

    public static void main(String[] args) {

        List<CompletableFuture<String>> tasks = new ArrayList<>();
        for (int i = 0; i < QUERY_COUNT; i++) {
            tasks.add(CompletableFuture.supplyAsync(() -> {
                DatabaseConnection connection = ConnectionPool.getInstance().getConnection();
                String result = connection.queryDatabase("query");
                ConnectionPool.getInstance().releaseConnection(connection);
                return result;
            }));
        }
        CompletableFuture.allOf((CompletableFuture<?>[]) tasks.toArray()).join();
    }
}
