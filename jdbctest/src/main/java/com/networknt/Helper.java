package com.networknt;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by steve on 14/05/17.
 */
public class Helper {
    /**
     * Returns a random integer that is a suitable value for both the {@code id}
     * and {@code randomNumber} properties of a world object.
     *
     * @return a random world number
     */
    public static int randomWorld() {
        return 1 + ThreadLocalRandom.current().nextInt(10000);
    }

    private static final int cpuCount = Runtime.getRuntime().availableProcessors();

    // todo: parameterize multipliers
    /*
    public static ExecutorService EXECUTOR =
            new ThreadPoolExecutor(
                    cpuCount * 2, cpuCount * 25, 200, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(cpuCount * 100),
                    new ThreadPoolExecutor.CallerRunsPolicy());
    */

    public static final Executor smallExecutor =
            Executors.newFixedThreadPool(1000,
                    new ThreadFactory() {
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        }
                    });

    public static final Executor bigExecutor =
            Executors.newFixedThreadPool(2000,
                    new ThreadFactory() {
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        }
                    });

    public static final Executor extraExecutor =
            Executors.newFixedThreadPool(5000,
                    new ThreadFactory() {
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        }
                    });


    public static World selectWorld(DataSource ds) {
        try (final Connection connection = ds.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM world WHERE id = ?",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                statement.setInt(1, Helper.randomWorld());
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return new World(
                            resultSet.getInt("id"),
                            resultSet.getInt("randomNumber"));
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static World selectWorld(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM world WHERE id = ?",
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, Helper.randomWorld());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return new World(
                        resultSet.getInt("id"),
                        resultSet.getInt("randomNumber"));
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static World selectWorld(Connection connection, PreparedStatement statement) {
        try {
            statement.setInt(1, Helper.randomWorld());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return new World(
                        resultSet.getInt("id"),
                        resultSet.getInt("randomNumber"));
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static World updateWorld(DataSource ds) {
        World world;
        try (final Connection connection = ds.getConnection()) {
            try (PreparedStatement update = connection.prepareStatement(
                    "UPDATE world SET randomNumber = ? WHERE id= ?")) {
                try (PreparedStatement query = connection.prepareStatement(
                        "SELECT * FROM world WHERE id = ?",
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {

                    query.setInt(1, Helper.randomWorld());
                    try (ResultSet resultSet = query.executeQuery()) {
                        resultSet.next();
                        world = new World(
                                resultSet.getInt("id"),
                                resultSet.getInt("randomNumber"));
                    }
                }
                world.randomNumber = Helper.randomWorld();
                update.setInt(1, world.randomNumber);
                update.setInt(2, world.id);
                update.executeUpdate();
                return world;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void batchUpdateWorld(DataSource ds, List<World> worlds) {
        try (final Connection connection = ds.getConnection()) {
            try (PreparedStatement update = connection.prepareStatement(
                    "UPDATE world SET randomNumber = ? WHERE id= ?")) {
                connection.setAutoCommit(false);
                for(World world: worlds) {
                    update.setInt(1, Helper.randomWorld());
                    update.setInt(2, world.id);
                    update.addBatch();
                }
                update.executeBatch();
                connection.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                futures.stream().
                        map(future -> future.join()).
                        collect(Collectors.<T>toList())
        );
    }

}
