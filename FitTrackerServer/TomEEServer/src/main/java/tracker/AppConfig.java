package tracker;

import tracker.authentication.AuthenticationRest;
import tracker.goal.GoalRest;
import tracker.goal.interceptors.GoalListReader;
import tracker.goal.interceptors.GoalListWriter;
import tracker.goal.interceptors.GoalReader;
import tracker.goal.interceptors.GoalWriter;
import tracker.sharedsportactivities.SharedSportActivitiesRest;
import tracker.sportactivity.interceptors.SportActivityListReader;
import tracker.sportactivity.interceptors.SportActivityListWriter;
import tracker.sportactivity.interceptors.SportActivityReader;
import tracker.sportactivity.interceptors.SportActivityWriter;
import tracker.sync.Sync;
import tracker.security.JWTAuthFilter;
import tracker.sync.SyncFilter;
import tracker.settings.SettingsRest;
import tracker.sportactivity.SportActivityRest;
import tracker.sync.SynchronizationRest;
import tracker.weight.WeightRest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.Set;

@ApplicationPath("/api/*")
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
        // rest


        //filters
//        resources.add(JWTAuthFilter.class);
//        resources.add(SyncFilter.class);
//
//        resources.add(Sync.class);

        //interceptors
        resources.add(GoalListReader.class);
        resources.addAll(Arrays.asList(
                //rest
                AuthenticationRest.class,
                SportActivityRest.class,
                SynchronizationRest.class,
                GoalRest.class,
                SharedSportActivitiesRest.class,
                WeightRest.class,
                SettingsRest.class,

                //interceptors
                GoalListReader.class,
                GoalListWriter.class,
                GoalReader.class,
                GoalWriter.class,
                SportActivityListReader.class,
                SportActivityListWriter.class,
                SportActivityReader.class,
                SportActivityWriter.class
        ));

        return resources;
    }

}

