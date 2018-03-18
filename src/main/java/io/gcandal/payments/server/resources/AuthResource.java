package io.gcandal.payments.server.resources;

import com.google.common.collect.ImmutableMap;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.gcandal.payments.server.core.User;
import io.gcandal.payments.server.db.UserDAO;
import io.gcandal.payments.server.utils.AuthUtils;
import org.jose4j.lang.JoseException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path(AuthResource.AUTH_URL)
@Produces(APPLICATION_JSON)
public class AuthResource {

    private final byte[] tokenSecret;
    private final UserDAO userDAO;
    static final String AUTH_URL = "auth/";

    public AuthResource(final byte[] tokenSecret, final UserDAO userDAO) {
        this.tokenSecret = tokenSecret;
        this.userDAO = userDAO;
    }

    @POST
    @Path("/login")
    @UnitOfWork
    public Map<String, String> login(final User userCredentials) {
        final List<User> users = this.userDAO.getByName(userCredentials.getName());

        if (users.isEmpty()) {
            throw new WebApplicationException("Invalid credentials.", Response.Status.UNAUTHORIZED);
        }

        final User user = users.get(0);
        if (!AuthUtils.matches(user.getPassword(), userCredentials.getPassword(), user.getSalt())) {
            throw new WebApplicationException("Invalid credentials.", Response.Status.UNAUTHORIZED);
        }

        try {
            final String token = AuthUtils.getValidToken(this.tokenSecret, user.getName());
            return ImmutableMap.of(
                    "token", token
            );
        } catch (final JoseException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/check-token")
    public User get(@Auth final Principal principal) {
        return (User) principal;
    }
}
