package com.networknt;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
public class QueriesBench extends BenchBase
{
    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public List<World> queryWithDs() throws SQLException
    {
        int queries = 20;

        List<CompletableFuture<World>> worlds = IntStream.range(0, queries)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> Helper.selectWorld(DS), Helper.smallExecutor))
                .collect(Collectors.toList());
        List<World> list = worlds.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return list;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public List<World> queryWithConnection() throws SQLException
    {
        int queries = 20;
        try (final Connection connection = DS.getConnection()) {
            List<CompletableFuture<World>> worlds = IntStream.range(0, queries)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> Helper.selectWorld(connection), Helper.smallExecutor))
                    .collect(Collectors.toList());
            List<World> list = worlds.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            return list;

        }
    }

}
