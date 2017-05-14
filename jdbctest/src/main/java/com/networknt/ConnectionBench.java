package com.networknt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

/*
# Run complete. Total time: 00:03:46

Benchmark                       (driver)   Mode  Cnt     Score     Error   Units
ConnectionBench.cycleCnnection        pg  thrpt   80  8120.358 ± 270.629  ops/ms
ConnectionBench.cycleCnnection      pgng  thrpt   80  8205.575 ± 262.496  ops/ms

 */

@Warmup(iterations=3)
@Measurement(iterations=8)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ConnectionBench extends BenchBase
{
    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public static Connection cycleCnnection() throws SQLException
    {
        Connection connection = DS.getConnection();
        connection.close();
        return connection;
    }
}
