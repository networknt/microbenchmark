package com.networknt;

import com.fizzed.rocker.runtime.DefaultRockerModel;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.util.ArrayList;
import java.util.List;


@State(Scope.Benchmark)
public class BenchBase
{

    List<Fortune> fortunes = new ArrayList<>();

    static Mustache mustache;
    static DefaultRockerModel rocker;


    @Setup(Level.Trial)
    public void setup(BenchmarkParams params)
    {
        fortunes.add(new Fortune(0, "Additional fortune added at request time."));
        fortunes.add(new Fortune(1, "fortune: No such file or directory"));
        fortunes.add(new Fortune(2, "A computer scientist is someone who fixes things that aren't broken."));
        fortunes.add(new Fortune(3, "After enough decimal places, nobody gives a damn."));
        fortunes.add(new Fortune(4, "A bad random number generator: 1, 1, 1, 1, 1, 4.33e+67, 1, 1, 1"));
        fortunes.add(new Fortune(5, "A computer program does what you tell it to do, not what you want it to do."));
        fortunes.add(new Fortune(6, "Emacs is a nice operating system, but I prefer UNIX. — Tom Christaensen"));
        fortunes.add(new Fortune(7, "Any program that runs right is obsolete."));
        fortunes.add(new Fortune(8, "A list is only as strong as its weakest link. — Donald Knuth"));
        fortunes.add(new Fortune(9, "Feature: A bug with seniority."));
        fortunes.add(new Fortune(10, "Computers make very fast, very accurate mistakes."));
        fortunes.add(new Fortune(11, "<script>alert(\"This should not be displayed in a browser alert box.\");</script>"));
        fortunes.add(new Fortune(12, "フレームワークのベンチマーク"));

        setupMustache();
    }

    @TearDown(Level.Trial)
    public void teardown()
    {
    }

    protected void setupMustache()
    {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        mustache = mustacheFactory.compile("fortunes.mustache");
    }
}
