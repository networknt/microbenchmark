package com.networknt;

import org.openjdk.jmh.annotations.*;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

@Warmup(iterations=3)
@Measurement(iterations=10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MyBenchmark1 {

    private static final ByteBuffer buffer;
    private static final String MESSAGE = "Hello, World!";

    static {
        buffer = ByteBuffer.allocateDirect(MESSAGE.length());
        try {
            buffer.put(MESSAGE.getBytes("US-ASCII"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        buffer.flip();
    }

    @Benchmark
    public ByteBuffer test1() {
        return buffer.duplicate();
    }

}
