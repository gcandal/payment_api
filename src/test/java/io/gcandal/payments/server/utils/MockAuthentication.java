package io.gcandal.payments.server.utils;


import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;
import io.dropwizard.auth.AuthDynamicFeature;
import io.gcandal.payments.server.core.User;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import java.util.Optional;

public final class MockAuthentication {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTUyMTMwMzEwNX0.CS_mvDH8t5IT91P-U2vONj7CFCozj6QtgptAHcfG214";
    private static final String SECRET_FOR_VALID_TOKEN = "dfwzsdzwh823zebdwdz772632gdsbdefigh34ty73gfh34uifh34f";

    public static AuthDynamicFeature ALWAYS_AUTH_FEAUTRE = new AuthDynamicFeature(
            new JwtAuthFilter.Builder<User>()
                    .setJwtConsumer(
                            new JwtConsumerBuilder()
                                    .setVerificationKey(new HmacKey(
                                            SECRET_FOR_VALID_TOKEN.getBytes()))
                                    .setEvaluationTime(NumericDate.fromMilliseconds(0))
                                    .build()
                            )
                    .setRealm("realm")
                    .setPrefix("Bearer")
                    .setAuthenticator(credentials -> Optional.of(new User()))
                    .buildAuthFilter()
    );

    public static Invocation.Builder requestWithAuth(final Invocation.Builder requestBuilder) {
        return requestBuilder.header(HttpHeaders.AUTHORIZATION, String.format("BEARER %s", VALID_TOKEN));
    }
}
