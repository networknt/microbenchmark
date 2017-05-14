package com.networknt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StatementBench extends BenchBase
{
    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public Statement cycleStatement(Blackhole bh, ConnectionState state) throws SQLException
    {
        Statement statement = state.connection.createStatement();
        bh.consume(statement.execute("INSERT INTO test (column) VALUES (?)"));
        statement.close();
        return statement;
    }

    @State(Scope.Thread)
    public static class ConnectionState
    {
        Connection connection;

        @Setup(Level.Iteration)
        public void setup() throws SQLException
        {
            connection = DS.getConnection();
        }

        @TearDown(Level.Iteration)
        public void teardown() throws SQLException
        {
            connection.close();
        }
    }
}
