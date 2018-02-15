package main.java.com.traker;

import main.java.com.traker.Markers.*;
import main.java.com.traker.filters.JWTAuthFilter;
import main.java.com.traker.filters.SyncFilter;
import main.java.com.traker.rest.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/*")
public class AppConfig extends Application
{

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
        resources.add(main.java.com.traker.rest.Settings.class);
        resources.add(org.glassfish.jersey.server.mvc.jsp.JspMvcFeature.class);
        return resources;
    }

}

