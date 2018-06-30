package tracker.filters;

import tracker.Markers.UserWriting;
import tracker.Entities.Users.GenericUser;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@UserWriting
public class UserWritingFilter implements ContainerRequestFilter {

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        user.setWriting(true);
    }
}
