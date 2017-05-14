package com.networknt;

import java.sql.*;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Warmup(iterations=3)
@Measurement(iterations=8)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class QueryBench extends BenchBase
{
    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public World query() throws SQLException
    {
        World world;
        try (final Connection connection = DS.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM world WHERE id = ?",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                statement.setInt(1, Helper.randomWorld());
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    world = new World(resultSet.getInt("id"), resultSet.getInt("randomNumber"));
                }
            }
        }
        return world;
    }
}
