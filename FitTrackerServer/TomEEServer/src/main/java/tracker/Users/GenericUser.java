package tracker.Users;

import java.util.UUID;

public class GenericUser extends User {

    private long clientSyncTimestamp;
    private long oldServerSyncTimestamp;
    private long newServerTimestamp;
    private boolean isWriting;
    private boolean isNew;

    public GenericUser(){}

    public GenericUser(String id, String email) {
        super(id);
        this.email = email;
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
}
