To run the test:

```
mvn clean install
java -jar target/benchmarks.jar
```
And the result is 

```
# Run complete. Total time: 00:01:00

Benchmark            Mode  Cnt      Score      Error   Units
MyBenchmark1.test1  thrpt   28  90767.210 ± 9739.893  ops/ms
MyBenchmark2.test2  thrpt   28  74093.091 ± 7471.619  ops/ms

```
