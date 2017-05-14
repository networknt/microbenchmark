package com.networknt.benchmark.helloworld;

import com.networknt.benchmark.All;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by steve on 13/05/17.
 */
@State(Scope.Thread)
public class DeserDslJson {
}
