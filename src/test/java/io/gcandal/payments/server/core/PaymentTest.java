package io.gcandal.payments.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final Payment PAYMENT_IN_FIXTURE = new Payment(
            UUID.fromString("4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"),
            "0",
            UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"),
            new Attributes(100.21)
    );

    private static final String FIXTURE_PATH = "fixtures/payment.json";

    @Test
    public void serializesToJSON() throws Exception {
        final String fromFixture = MAPPER.writeValueAsString(
                MAPPER.readValue(FixtureHelpers.fixture(FIXTURE_PATH), Payment.class)
        );

        assertThat(MAPPER.writeValueAsString(PAYMENT_IN_FIXTURE))
                .isEqualTo(fromFixture);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final Payment fromFixture = MAPPER.readValue(FixtureHelpers.fixture(FIXTURE_PATH), Payment.class);

        assertThat(fromFixture)
                .isEqualTo(PAYMENT_IN_FIXTURE);
    }
}
