package io.gcandal.payments.server.resources;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class SmoothDelete<O> extends AbstractDAO<O> {

    public SmoothDelete(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public O delete(final UUID id) {
        final O object = get(id);

        if (object == null) {
            final String errorMessage = String.format(
                    "The content ID '%s' references an object which doesn't exist.",
                   id
            );
            throw new WebApplicationException(errorMessage, Response.Status.NOT_FOUND);
        }

        currentSession().delete(object);
        return object;
    }
}
