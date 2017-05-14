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

/*

Benchmark                           (driver)   Mode  Cnt    Score    Error  Units
        UpdatesBench.updatesBig                   pg  thrpt   80  394.355 ± 14.371  ops/s
        UpdatesBench.updatesSmall                 pg  thrpt   80  388.773 ± 15.392  ops/s
        UpdatesBench.updatesWithBatchBig          pg  thrpt   80  253.497 ±  5.272  ops/s
        UpdatesBench.updatesWithBatchSmall        pg  thrpt   80  272.034 ±  3.634  ops/s
*/

@State(Scope.Benchmark)
@Warmup(iterations=3)
@Measurement(iterations=8)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class UpdatesBench extends BenchBase
{
    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public List<World> updatesSmall() throws Exception
    {
        int queries = 20;
        List<CompletableFuture<World>> worlds = IntStream.range(0, queries)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> Helper.updateWorld(DS), Helper.smallExecutor))
                .collect(Collectors.toList());

        CompletableFuture<List<World>> allDone = Helper.sequence(worlds);
        return allDone.get();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public List<World> updatesBig() throws Exception
    {
        int queries = 20;
        List<CompletableFuture<World>> worlds = IntStream.range(0, queries)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> Helper.updateWorld(DS), Helper.bigExecutor))
                .collect(Collectors.toList());

        CompletableFuture<List<World>> allDone = Helper.sequence(worlds);
        return allDone.get();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public List<World> updatesExtra() throws Exception
    {
        int queries = 20;
        List<CompletableFuture<World>> worlds = IntStream.range(0, queries)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> Helper.updateWorld(DS), Helper.extraExecutor))
                .collect(Collectors.toList());

        CompletableFuture<List<World>> allDone = Helper.sequence(worlds);
        return allDone.get();
    }

    /*
    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public List<World> updatesWithBatchSmall() throws SQLException
    {
        int queries = 20;
        try (final Connection connection = DS.getConnection()) {
            List<CompletableFuture<World>> worlds = IntStream.range(0, queries)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> Helper.selectWorld(connection), Helper.smallExecutor))
                    .collect(Collectors.toList());
            List<World> list = worlds.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            Helper.batchUpdateWorld(DS, list);
            return list;
        }
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.INLINE)
    public List<World> updatesWithBatchBig() throws SQLException
    {
        int queries = 20;
        try (final Connection connection = DS.getConnection()) {
            List<CompletableFuture<World>> worlds = IntStream.range(0, queries)
                    .mapToObj(i -> CompletableFuture.supplyAsync(() -> Helper.selectWorld(connection), Helper.bigExecutor))
                    .collect(Collectors.toList());
            List<World> list = worlds.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            Helper.batchUpdateWorld(DS, list);
            return list;
        }
    }
    */


}
