package com.networknt.benchmark.helloworld;

import com.jsoniter.DecodingMode;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
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
 Result "com.networknt.benchmark.helloworld.SerJsoniter.ser":
 64285.281 ±(99.9%) 644.151 ns/op [Average]
 (min, avg, max) = (62118.317, 64285.281, 76095.411), stdev = 2727.377
 CI (99.9%): [63641.130, 64929.432] (assumes normal distribution)


 # Run complete. Total time: 00:06:42

 Benchmark        Mode  Cnt      Score     Error  Units
 SerJsoniter.ser  avgt  200  64285.281 ± 644.151  ns/op

 */

@State(Scope.Thread)
public class SerJsoniter {

    private HelloWorld helloWorld;
    private JsonStream stream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Setup(Level.Trial)
    public void benchSetup(BenchmarkParams params) {
//        JsonIterator.enableAnnotationSupport();
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        helloWorld = HelloWorld.createHelloWorld();
        stream = new JsonStream(null, 512);
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void ser(Blackhole bh) throws IOException {
        for (int i = 0; i < 1000; i++) {
            byteArrayOutputStream.reset();
            stream.reset(byteArrayOutputStream);
            stream.writeVal(helloWorld);
            stream.flush();
            bh.consume(byteArrayOutputStream);
        }
    }

    @Test
    public void test() throws IOException {
        benchSetup(null);
        byteArrayOutputStream.reset();
        stream.reset(byteArrayOutputStream);
        stream.writeVal(helloWorld);
        stream.flush();
        assertEquals("{\"message\":\"Hello World!\"}", byteArrayOutputStream.toString());
    }

    public static void main(String[] args) throws IOException, RunnerException {
        All.loadJMH();
        Main.main(new String[]{
                "helloworld.SerJsoniter",
                "-i", "5",
                "-wi", "5",
                "-f", "1",
        });
    }
}