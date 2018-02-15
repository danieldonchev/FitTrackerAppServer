package main.java.com.traker.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import main.java.com.traker.Authenticate.Authenticator;
import main.java.com.traker.DAO.DAOFactory;
import main.java.com.traker.DAO.UserSyncDAO;
import main.java.com.traker.Markers.Secured;
import main.java.com.traker.Users.GenericUser;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.UUID;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {

        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();
        String syncVersion =  requestContext.getHeaderString("Sync-Time");

        try
        {
            // Validate token
            Jws<Claims> tokenClaims = Authenticator.validateJwt(token);
            String username = (String) tokenClaims.getBody().get("email");
            String id = (String) tokenClaims.getBody().get("userID");

            // Get server last sync time
            DAOFactory daoFactory = new DAOFactory();
            UserSyncDAO userSyncDAO = daoFactory.getUserSyncDAO();
            long serverVersion = userSyncDAO.getLastModifiedTime(id);

            // Create GenericUser from access token information and server information
            GenericUser user = new GenericUser(UUID.fromString(id), username, Long.parseLong(syncVersion));
            user.setOldServerSyncTimestamp(serverVersion);
            long timestamp = Instant.now().toEpochMilli();
            user.setNewServerTimestamp(timestamp);

            // Overwrite the context principal with GenericUser
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal(){

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
