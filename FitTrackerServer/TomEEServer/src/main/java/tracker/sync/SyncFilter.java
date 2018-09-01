package tracker.sync;

import tracker.authentication.users.UserPrincipal;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Sync
public class SyncFilter implements ContainerResponseFilter {

    private UserPrincipal user;

    public SyncFilter() {
    }

    @Inject
    public SyncFilter(UserPrincipal user) {
        this.user = user;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        if (this.user.getClientSyncTimestamp() < this.user.getOldServerSyncTimestamp()) {
            containerResponseContext.getHeaders().add("Should-Sync", true);
        }
        if (this.user.isWriting()) {
            containerResponseContext.getHeaders().add("Sync-Time", this.user.getNewServerTimestamp());
        } else {
            containerResponseContext.getHeaders().add("Sync-Time", this.user.getOldServerSyncTimestamp());
        }
    }
}
