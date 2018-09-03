package tracker.authentication.users;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.security.Principal;
import java.util.UUID;

@Named
@SessionScoped
public class UserPrincipal implements Serializable{

    private UUID id;
    private String name;
    private String email;
    private long clientSyncTimestamp;
    private long oldServerSyncTimestamp;
    private long newServerTimestamp;
    private boolean isWriting;
    private boolean isNew;
    private boolean isMobile;

    public UserPrincipal(){}

    public UserPrincipal(String id, String email) {
        this.id = UUID.fromString(id);
        this.email = email;
    }

    public UserPrincipal(String id, String email, long syncVersion) {
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
