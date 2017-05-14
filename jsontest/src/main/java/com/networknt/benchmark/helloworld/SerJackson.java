package com.networknt.benchmark.helloworld;

/**
 * Created by steve on 13/05/17.
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
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

/**
Result "com.networknt.benchmark.helloworld.SerJackson.ser":
  184939.591 ±(99.9%) 5302.442 ns/op [Average]
  (min, avg, max) = (136936.931, 184939.591, 254537.956), stdev = 22450.871
  CI (99.9%): [179637.149, 190242.033] (assumes normal distribution)

Benchmark       Mode  Cnt       Score      Error  Units
SerJackson.ser  avgt  200  184939.591 ± 5302.442  ns/op

 */

@State(Scope.Thread)
public class SerJackson {

    private ObjectMapper objectMapper;
    private ByteArrayOutputStream byteArrayOutputStream;
    private HelloWorld helloWorld;

    @Setup(Level.Trial)
    public void benchSetup(BenchmarkParams params) {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new AfterburnerModule());
        byteArrayOutputStream = new ByteArrayOutputStream();
        helloWorld = HelloWorld.createHelloWorld();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void ser(Blackhole bh) throws IOException {
        for (int i = 0; i < 1000; i++) {
            byteArrayOutputStream.reset();
            objectMapper.writeValue(byteArrayOutputStream, helloWorld);
            bh.consume(byteArrayOutputStream);
        }
    }

    @Test
    public void test() throws IOException {
        benchSetup(null);
        byteArrayOutputStream.reset();
        objectMapper.writeValue(byteArrayOutputStream, helloWorld);
        assertEquals("{\"message\":\"Hello World!\"}", byteArrayOutputStream.toString());
    }

    public static void main(String[] args) throws IOException, RunnerException {
        All.loadJMH();
        Main.main(new String[]{
                "helloworld.SerJackson",
                "-i", "5",
                "-wi", "5",
                "-f", "1",
        });
    }
}
