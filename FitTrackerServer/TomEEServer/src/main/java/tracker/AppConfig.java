package tracker;

import tracker.Markers.Sync;
import tracker.filters.JWTAuthFilter;
import tracker.filters.SyncFilter;
import tracker.rest.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/*")
public class AppConfig extends Application {

    public AppConfig() {
        super();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return getRestResourceClasses();
    }

    private Set<Class<?>> getRestResourceClasses() {

        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(UserAuthentication.class);
        resources.add(JWTAuthFilter.class);
        resources.add(UserActivity.class);
        resources.add(SyncFilter.class);
        resources.add(Sync.class);
        resources.add(Synchronization.class);
        resources.add(Goals.class);
        resources.add(PeopleActivities.class);
        resources.add(Weights.class);
        resources.add(tracker.rest.Settings.class);
        return resources;
    }

}

