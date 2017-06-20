To run the test:

```
mvn clean install
java -jar target/benchmarks.jar
```
And the result is 

```
# Run complete. Total time: 00:01:00

Benchmark                      Mode  Cnt      Score     Error   Units
CacheJwt.verifyWithCache      thrpt   28  23782.696 ± 501.960  ops/ms
VerifyJwt.verifyWithoutCache  thrpt   28      4.604 ±   0.114  ops/ms

```
