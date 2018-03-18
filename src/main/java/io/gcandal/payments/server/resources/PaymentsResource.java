package io.gcandal.payments.server.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.gcandal.payments.server.core.Payment;
import io.gcandal.payments.server.db.PaymentDAO;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;

@Path(PaymentsResource.PAYMENTS_URL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentsResource {
    static final String PAYMENTS_URL = "payments/";
    private final PaymentDAO paymentDAO;

    public PaymentsResource(final PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @POST
    @UnitOfWork
    public Payment create(@Auth final Principal principal,
                          @Valid final Payment payment) {

        return this.paymentDAO.create(payment);
    }

    @GET
    @UnitOfWork
    public List<Payment> list(@Auth final Principal principal) {
        return this.paymentDAO.list();
    }
}
