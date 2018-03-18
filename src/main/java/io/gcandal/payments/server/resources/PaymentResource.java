package io.gcandal.payments.server.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.gcandal.payments.server.core.Payment;
import io.gcandal.payments.server.db.PaymentDAO;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.UUID;

@Path(PaymentResource.PAYMENT_URL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource implements SmoothUpdate<Payment, PaymentDAO> {
    static final String PAYMENT_URL = PaymentsResource.PAYMENTS_URL + "{id}";
    private final PaymentDAO paymentDAO;

    public PaymentResource(final PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @GET
    @UnitOfWork
    public Payment get(@Auth final Principal principal,
                       @PathParam("id") final UUID id) {
        return this.paymentDAO.get(id);
    }

    @PUT
    @UnitOfWork
    public Payment update(@Auth final Principal principal,
                          @PathParam("id") final UUID id,
                          @Valid final Payment payment) {
        return update(id, payment, this.paymentDAO);
    }

    @DELETE
    @UnitOfWork
    public Payment delete(@Auth final Principal principal,
                          @PathParam("id") final UUID id) {
        return this.paymentDAO.delete(id);
    }
}
