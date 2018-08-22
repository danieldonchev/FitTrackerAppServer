package tracker.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import tracker.Authenticate.TokenAuthenticator;
import tracker.DAO.DaoServices.SynchronizationService;
import tracker.DAO.DaoServices.SynchronizationServiceImpl;
import tracker.Entities.GenericUser;
import tracker.Entities.ModifiedTimes;
import tracker.Markers.Secured;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;
import java.time.Instant;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter {

    private SynchronizationService service;

    public JWTAuthFilter() {
    }

    @Inject
    public JWTAuthFilter(SynchronizationService service) {
        this.service = service;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        boolean isMobile = false;
        if(requestContext.getHeaderString("User-Agent").contains("Mobile")) {
            isMobile = true;
        }

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
        String syncVersion = requestContext.getHeaderString("Sync-Time");

        try {

            // Validate token
            TokenAuthenticator authenticator = new TokenAuthenticator();
            Jws<Claims> tokenClaims = authenticator.validateJwt(token);
            String username = (String) tokenClaims.getBody().get("email");
            String id = (String) tokenClaims.getBody().get("userID");

            GenericUser user = new GenericUser(id, username, syncVersion != null ? Long.parseLong(syncVersion) : 0);
            user.setMobile(isMobile);

            // Get server last sync time
            ModifiedTimes times = this.service.getTimes(user);
            long serverVersion = times.getLastModified();

            // Create GenericUser from access token information and server information

            user.setOldServerSyncTimestamp(serverVersion);
            long timestamp = Instant.now().toEpochMilli();
            user.setNewServerTimestamp(timestamp);

            // Overwrite the context principal with GenericUser
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return user;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return null;
                }
            });

        } catch (NotAuthorizedException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
