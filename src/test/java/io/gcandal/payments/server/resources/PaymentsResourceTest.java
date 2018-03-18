package io.gcandal.payments.server.resources;

import com.google.common.collect.ImmutableList;
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

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentsResourceTest {
    private static final PaymentDAO dao = Mockito.mock(PaymentDAO.class);

    @ClassRule
    public static final ResourceTestRule RESOURCES = ResourceTestRule.builder()
            .addProvider(new AuthValueFactoryProvider.Binder<>(Principal.class))
            .addResource(MockAuthentication.ALWAYS_AUTH_FEAUTRE)
            .addResource(new PaymentsResource(dao))
            .build();

    private static final Payment PAYMENT = new Payment(
            UUID.fromString("4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"),
            "0",
            UUID.fromString("743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb"),
            new Attributes(100.21)
    );

    private static final List<Payment> PAYMENTS = ImmutableList.of(PAYMENT);

    @Before
    public void setup() {
        Mockito.when(dao.list())
                .thenReturn(PAYMENTS);
    }

    @After
    public void tearDown(){
        Mockito.reset(dao);
    }

    @Test
    public void testList() {
        final Invocation.Builder request = RESOURCES.target("payments").request();

        assertThat(MockAuthentication.requestWithAuth(request).get(new GenericType<List<Payment>>() {}))
                .isEqualTo(PAYMENTS);

        Mockito.verify(dao).list();
    }

    @Test
    public void testCreate() {
        final Invocation.Builder request = RESOURCES.target("payments").request();

        assertThat(MockAuthentication.requestWithAuth(request).post(Entity.json(PAYMENT)).getStatus())
                .isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        Mockito.verify(dao).create(PAYMENT);
    }


}
