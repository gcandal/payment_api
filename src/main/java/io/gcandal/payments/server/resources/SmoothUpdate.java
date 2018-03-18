package io.gcandal.payments.server.resources;

import io.gcandal.payments.server.core.Identifiable;
import io.gcandal.payments.server.db.Updatable;
import org.hibernate.StaleStateException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.UUID;

public interface SmoothUpdate<O extends Identifiable, D extends Updatable<O>> {
    default O update(final UUID id, final O object, final D dao) {
        if (object.getId() != null && !object.getId().equals(id)) {
            final String errorMessage = String.format(
                    "The content ID '%s' doesn't match with the one being passed in the path ('%s').",
                    object.getId(),
                    id
            );
            throw new WebApplicationException(errorMessage, Response.Status.BAD_REQUEST);
        }
        object.setId(id);

        if (dao.get(id) == null) {
            final String errorMessage = String.format(
                    "The content ID '%s' references an object which doesn't exist.",
                    object.getId()
            );
            throw new WebApplicationException(errorMessage, Response.Status.NOT_FOUND);
        }

        return dao.update(object);
    }
}
