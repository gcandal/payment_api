package io.gcandal.payments.server.resources;

import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import io.gcandal.payments.server.core.Attributes;
import io.gcandal.payments.server.core.Payment;
import io.gcandal.payments.server.db.PaymentDAO;
import io.gcandal.payments.server.utils.MockAuthentication;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentResourceTest {
    private static final PaymentDAO dao = Mockito.mock(PaymentDAO.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addProvider(new AuthValueFactoryProvider.Binder<>(Principal.class))
            .addResource(MockAuthentication.ALWAYS_AUTH_FEAUTRE)
            .addResource(new PaymentResource(dao))
            .build();

    private static final Payment PAYMENT = new Payment(
            UUID.fromString("4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"),
            "0",
            UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"),
            new Attributes(100.21)
    );

    @Before
    public void setup() {
        Mockito.when(dao.get(Mockito.eq(PAYMENT.getId().toString())))
                .thenReturn(PAYMENT);
    }

    @After
    public void tearDown(){
        Mockito.reset(dao);
    }

    @Test
    public void testGet() {
        final String paymentId = PAYMENT.getId().toString();
        final String getUrl = String.format("payments/%s", paymentId);

        assertThat(RESOURCES.target(getUrl).request().get(Payment.class))
                .isEqualTo(PAYMENT);

        Mockito.verify(dao).get(paymentId);
    }
}
