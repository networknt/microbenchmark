package com.networknt;

import org.openjdk.jmh.annotations.*;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.US_ASCII;

@Warmup(iterations=3)
@Measurement(iterations=10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MyBenchmark2 {

    private static final ByteBuffer buffer;

    static {
        String message = "Hello, World!";
        byte[] messageBytes = message.getBytes(US_ASCII);
        buffer = ByteBuffer.allocateDirect(messageBytes.length);
        buffer.put(messageBytes);
        buffer.flip();
    }

    @Benchmark
    public ByteBuffer test2() {
        return buffer.duplicate();
    }
}