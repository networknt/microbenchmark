package com.networknt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;

import javax.sql.DataSource;
import java.sql.SQLException;

@State(Scope.Benchmark)
public class BenchBase
{
    protected static final int MIN_POOL_SIZE = 0;

    //@Param({ "pg", "pgng" })
    @Param({ "pg"})

    public String driver;

    public static DataSource DS;

    @Setup(Level.Trial)
    public void setup(BenchmarkParams params)
    {
        switch (driver)
        {
            case "pg":
                setupPg();
                break;
            case "pgng":
                setupPgng();
                break;
        }
    }

    @TearDown(Level.Trial)
    public void teardown() throws SQLException
    {
        ((HikariDataSource) DS).close();
    }


    protected void setupPg()
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/hello_world");
        config.setUsername("postgres");
        config.setPassword("benchmarkdbpass");
        config.setMinimumIdle(MIN_POOL_SIZE);
        config.setMaximumPoolSize(100);

        config.setConnectionTimeout(8000);
        config.setAutoCommit(false);

        DS = new HikariDataSource(config);

    }

    protected void setupPgng()
    {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.impossibl.postgres.jdbc.PGDriver");
        config.setJdbcUrl("jdbc:pgsql://localhost:5432/hello_world");
        config.setUsername("postgres");
        config.setPassword("benchmarkdbpass");
        config.setMinimumIdle(MIN_POOL_SIZE);
        config.setMaximumPoolSize(100);
        config.setConnectionTimeout(8000);

        config.setAutoCommit(false);

        DS = new HikariDataSource(config);
    }

}
