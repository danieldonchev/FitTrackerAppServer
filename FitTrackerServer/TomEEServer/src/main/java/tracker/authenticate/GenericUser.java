package tracker.authenticate;


import javax.inject.Named;
import java.security.Principal;
import java.util.UUID;

@Named
public class GenericUser extends User implements Principal {

    private long clientSyncTimestamp;
    private long oldServerSyncTimestamp;
    private long newServerTimestamp;
    private boolean isWriting;
    private boolean isNew;
    private boolean isMobile;

    public GenericUser(){}

    public GenericUser(String id, String email) {
        super(UUID.fromString(id), email);
    }

    public GenericUser(String id, String email, long syncVersion) {
        this(id, email);
        this.clientSyncTimestamp = syncVersion;
    }

    public long getClientSyncTimestamp() {
        return clientSyncTimestamp;
    }

    public void setClientSyncTimestamp(long clientSyncTimestamp) {
        this.clientSyncTimestamp = clientSyncTimestamp;
    }

    public long getOldServerSyncTimestamp() {
        return oldServerSyncTimestamp;
    }

    public void setOldServerSyncTimestamp(long oldServerSyncTimestamp) {
        this.oldServerSyncTimestamp = oldServerSyncTimestamp;
    }

    public long getNewServerTimestamp() {
        return newServerTimestamp;
    }

    public void setNewServerTimestamp(long newServerTimestamp) {
        this.newServerTimestamp = newServerTimestamp;
    }

    public boolean isWriting() {
        return isWriting;
    }

    public void setWriting(boolean writing) {
        isWriting = writing;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isMobile() {
        return isMobile;
    }

    public void setMobile(boolean mobile) {
        isMobile = mobile;
    }
}
