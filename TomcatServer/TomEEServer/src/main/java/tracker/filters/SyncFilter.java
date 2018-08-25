package tracker.filters;

import tracker.Markers.Sync;
import tracker.Users.GenericUser;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Sync
public class SyncFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        GenericUser user = (GenericUser) containerRequestContext.getSecurityContext().getUserPrincipal();
        if(user.getClientSyncTimestamp() < user.getOldServerSyncTimestamp()){
            containerResponseContext.getHeaders().add("Should-Sync", true);
        }
        if(user.isWriting()){
            containerResponseContext.getHeaders().add("Sync-Time", user.getNewServerTimestamp());
        } else {
            containerResponseContext.getHeaders().add("Sync-Time", user.getOldServerSyncTimestamp());
        }
    }
}
