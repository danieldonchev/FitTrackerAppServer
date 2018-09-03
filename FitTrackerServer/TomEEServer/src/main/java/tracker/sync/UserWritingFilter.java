package tracker.sync;

import tracker.authentication.users.UserPrincipal;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@UserWriting
public class UserWritingFilter implements ContainerRequestFilter {

    private UserPrincipal user;

    public UserWritingFilter() { }

    @Inject
    public UserWritingFilter(UserPrincipal user) {
        this.user = user;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        this.user.setWriting(true);
    }
}
