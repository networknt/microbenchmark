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
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/*
Result "com.networknt.benchmark.helloworld.SerDslJson.ser":
  58547.204 ±(99.9%) 550.855 ns/op [Average]
  (min, avg, max) = (56490.234, 58547.204, 68607.413), stdev = 2332.353
  CI (99.9%): [57996.349, 59098.059] (assumes normal distribution)


# Run complete. Total time: 00:06:42

Benchmark       Mode  Cnt      Score     Error  Units
SerDslJson.ser  avgt  200  58547.204 ± 550.855  ns/op

 */
@State(Scope.Thread)
public class SerDslJson {

    private DslJson<Object> dsl = new DslJson();
    private JsonWriter jsonWriter;
    private ByteArrayOutputStream byteArrayOutputStream;
    private HelloWorld helloWorld;

    @Setup(Level.Trial)
    public void benchSetup(BenchmarkParams params) {
        helloWorld = HelloWorld.createHelloWorld();
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
            dsl.serialize(jsonWriter, helloWorld);
            jsonWriter.toStream(byteArrayOutputStream);
            bh.consume(byteArrayOutputStream);
        }
    }

    @Test
    public void test() throws IOException {
        benchSetup(null);
        byteArrayOutputStream.reset();
        byteArrayOutputStream.reset();
        dsl.serialize(jsonWriter, helloWorld);
        jsonWriter.toStream(byteArrayOutputStream);
        assertEquals("{\"message\":\"Hello World!\"}", byteArrayOutputStream.toString());
    }

    public static void main(String[] args) throws IOException, RunnerException {
        All.loadJMH();
        Main.main(new String[]{
                "helloworld.SerDslJson",
                "-i", "5",
                "-wi", "5",
                "-f", "1",
        });
    }


}