package com.networknt;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Created by steve on 14/05/17.
 */
public class JoseTest {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CacheJwt.class.getSimpleName())
                .include(VerifyJwt.class.getSimpleName())
                .warmupIterations(2)
                .measurementIterations(28)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
