package tracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import tracker.authentication.utils.TokenAuthenticator;
import tracker.sync.SynchronizationService;
import tracker.authentication.users.UserPrincipal;
import tracker.sync.ModifiedTimes;

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
import java.util.UUID;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter {

    private SynchronizationService service;
    private UserPrincipal user;

    public JWTAuthFilter() {
    }

    @Inject
    public JWTAuthFilter(SynchronizationService service, UserPrincipal user) {
        this.service = service;
        this.user = user;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Get the HTTP Authorization header from the request
        long timestamp = Instant.now().toEpochMilli();
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
            String email = (String) tokenClaims.getBody().get("email");
            String id = (String) tokenClaims.getBody().get("userID");

            this.user.setId(UUID.fromString(id));
            String testId = this.user.getId().toString();
            this.user.setEmail(email);
            this.user.setClientSyncTimestamp(syncVersion != null ? Long.parseLong(syncVersion) : 0);
            this.user.setMobile(isMobile);

            // Get server last sync time
            ModifiedTimes times = this.service.getTimes(user);
            long serverVersion = times.getLastModified();

            user.setOldServerSyncTimestamp(serverVersion);
            user.setNewServerTimestamp(timestamp);

        } catch (NotAuthorizedException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
