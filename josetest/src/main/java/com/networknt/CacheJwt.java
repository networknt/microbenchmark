package com.networknt;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.networknt.exception.ExpiredTokenException;
import com.networknt.security.JwtHelper;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

@Warmup(iterations=3)
@Measurement(iterations=10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CacheJwt {
    static String jwt = "eyJraWQiOiIxMDAiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ1cm46Y29tOm5ldHdvcmtudDpvYXV0aDI6djEiLCJhdWQiOiJ1cm46Y29tLm5ldHdvcmtudCIsImV4cCI6MTgxMzI2OTI4OCwianRpIjoiSThnTy1FVGZvQzUzbFVxeF9POFlrdyIsImlhdCI6MTQ5NzkwOTI4OCwibmJmIjoxNDk3OTA5MTY4LCJ2ZXJzaW9uIjoiMS4wIiwidXNlcl9pZCI6IlN0ZXZlIiwidXNlcl90eXBlIjoiRU1QTE9ZRUUiLCJjbGllbnRfaWQiOiJmN2Q0MjM0OC1jNjQ3LTRlZmItYTUyZC00YzU3ODc0MjFlNzIiLCJzY29wZSI6WyJhcGlfYS53IiwiYXBpX2IudyIsImFwaV9jLnciLCJhcGlfZC53Iiwic2VydmVyLmluZm8uciJdfQ.ksa_oFlggIepp9DgG8-dgFUL2cuRTo_KOeDJNeKIFtVh5M7F8aUDdgBzmXcvSGrP2A33xgEaaVFHx_fXRbUrgteoFogr2aopPq5_dgUm24YK58RTH6sqHhGkyHdG0WOwZXflPvJ4BUh3pzh2_EPps4vMQTeVKdtZ9KyelYpRsSyHwO7D1kQkV5Tqlm9m__J5YJPfGcTNkQT_Z9ECJId6TvJbPfb-62yYTGOR8joq_eZUJLlH_gyswbpc_M0J_U27sBekikNPfvT3QDq7FEmVISKqcPQ_EB9-zsnyFiJzYKApO0tU1kHsZKaoIzpQkS15SuxWL-ElLV8qytRz9JnCxQ";

    static LoadingCache<String, JwtClaims> cache;

    static {
        CacheLoader<String, JwtClaims> loader;
        loader = new CacheLoader<String, JwtClaims>() {
            @Override
            public JwtClaims load(String jwt) throws InvalidJwtException, ExpiredTokenException {
                return JwtHelper.verifyJwt(jwt);
            }
        };

        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(loader);
    }

    @Benchmark
    public JwtClaims verifyWithCache() {
        return cache.getUnchecked(jwt);
    }

    @Test
    public void testVerifyJwt() throws InvalidJwtException, ExpiredTokenException {
        JwtClaims jwtClaims = JwtHelper.verifyJwt(jwt);
        System.out.println("jwtClaims" + jwtClaims);
    }

}
