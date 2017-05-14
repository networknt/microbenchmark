package com.networknt.benchmark.helloworld;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.MapConverter;
import com.networknt.benchmark.All;
import org.junit.Test;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/*
Result "com.networknt.benchmark.helloworld.SerMapDslJson.ser":
  71435.186 ±(99.9%) 722.793 ns/op [Average]
  (min, avg, max) = (68545.841, 71435.186, 88853.793), stdev = 3060.351
  CI (99.9%): [70712.392, 72157.979] (assumes normal distribution)


# Run complete. Total time: 00:06:42

Benchmark          Mode  Cnt      Score     Error  Units
SerMapDslJson.ser  avgt  200  71435.186 ± 722.793  ns/op

 */
@State(Scope.Thread)
public class SerMapDslJson {

    private DslJson<Object> dsl = new DslJson();
    private JsonWriter jsonWriter;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Map map;

    @Setup(Level.Trial)
    public void benchSetup(BenchmarkParams params) {
        map = Collections.singletonMap("message", "Hello World!");
        jsonWriter = dsl.newWriter(512);
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void ser(Blackhole bh) throws IOException {
        for (int i = 0; i < 1000; i++) {
            jsonWriter.reset();
            byteArrayOutputStream.reset();
            MapConverter.serialize(map, jsonWriter);
            jsonWriter.toStream(byteArrayOutputStream);
            bh.consume(byteArrayOutputStream);
        }
    }

    @Test
    public void test() throws IOException {
        benchSetup(null);
        byteArrayOutputStream.reset();
        byteArrayOutputStream.reset();
        MapConverter.serialize(map, jsonWriter);
        jsonWriter.toStream(byteArrayOutputStream);
        assertEquals("{\"message\":\"Hello World!\"}", byteArrayOutputStream.toString());
    }

    public static void main(String[] args) throws IOException, RunnerException {
        All.loadJMH();
        Main.main(new String[]{
                "helloworld.SerMapDslJson",
                "-i", "5",
                "-wi", "5",
                "-f", "1",
        });
    }
}