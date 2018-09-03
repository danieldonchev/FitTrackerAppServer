package tracker.sync;

import com.tracker.shared.entities.SportActivityWeb;
import org.json.JSONArray;
import org.json.JSONObject;
import tracker.authentication.users.UserPrincipal;
import tracker.goal.Goal;
import tracker.goal.interceptors.GoalListReaderInterceptor;
import tracker.goal.interceptors.GoalListWriterInterceptor;
import tracker.security.Secured;
import tracker.settings.Details;
import tracker.sportactivity.interceptors.SportActivityListReaderInterceptor;
import tracker.sportactivity.interceptors.SportActivityListWriterInterceptor;
import tracker.sportactivity.SportActivity;
import tracker.utils.API;
import tracker.settings.UserSettignsService;
import tracker.weight.Weight;
import tracker.weight.interceptors.WeightListReaderInterceptor;
import tracker.weight.interceptors.WeightListWriterInterceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Secured
@Sync
@Path(API.sync)
public class SynchronizationRest {

    private SynchronizationService service;
    private UserSettignsService settingsService;
    private UserPrincipal user;

    public SynchronizationRest() { }

    @Inject
    public SynchronizationRest(SynchronizationService service,
                               UserSettignsService settingsService, UserPrincipal user) {
        this.service = service;
        this.settingsService = settingsService;
        this.user = user;
    }

    @GET
    @Path(API.syncCheck)
    @Produces(MediaType.APPLICATION_JSON)
    public Response shouldSync(@Context HttpServletRequest req) {
        return Response.ok().build();
    }

    /*
    Gets missing activities
     */
    @GET
    @Path(API.missingSportActivities)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @SportActivityListWriterInterceptor
    public Response getSportActivities(@Context HttpServletResponse response) {

        List<Object> missingEntities = this.service.getMissingEntities(this.user, "user_sport_activity", SportActivity.class);

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(missingEntities).build();
    }

    @POST
    @Path(API.missingSportActivities)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @SportActivityListReaderInterceptor
    public Response insertSportActivities(List<SportActivity> sportActivities) {

        this.service.insertEntities(sportActivities);

        return Response.ok().build();
    }

    @GET
    @Path(API.deletedSportActivities)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeletedActivities() {
        List<String> sportActivities = this.service.getDeletedEntitiesId(this.user, "user_sport_activity");

        JSONArray jsonArray = new JSONArray();
        for(String id : sportActivities){
            jsonArray.put(id);
        }

        return Response.ok().entity(jsonArray.toString()).build();
    }

    @POST
    @Path(API.deletedSportActivities)
    @Consumes(MediaType.APPLICATION_JSON)
    @UserWriting
    public Response deleteActivities(String data) {

        JSONArray jsonArray = new JSONArray(data);
        ArrayList<String> ids = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            ids.add(jsonArray.get(i).toString());
        }
        this.service.deleteEntities(this.user, "user_sport_activity", ids);

        return Response.ok().build();
    }

    @GET
    @Path(API.missingGoals)
    @Produces(MediaType.APPLICATION_JSON)
    @GoalListWriterInterceptor
    public Response getGoals(@Context HttpServletResponse response) {

        List<Object> missingEntities = this.service.getMissingEntities(this.user, "user_sport_activity", SportActivity.class);

        return Response.ok().entity(missingEntities).build();
    }

    @POST
    @Path(API.missingGoals)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @GoalListReaderInterceptor
    public Response insertGoals(List<Goal> goals) {

        this.service.insertEntities(goals);
        return Response.ok().build();
    }

    @GET
    @Path(API.deletedGoals)
    @Produces(MediaType.APPLICATION_JSON)
    @GoalListWriterInterceptor
    public Response getDeletedGoals() {
        List<Object> missingEntities = this.service.getMissingEntities(this.user, "goals", Goal.class);
        return Response.ok().entity(missingEntities).build();
    }

    @POST
    @Path(API.deletedGoals)
    @Consumes(MediaType.APPLICATION_JSON)
    @UserWriting
    public Response deleteGoals(String data) {

        JSONArray jsonArray = new JSONArray(data);
        ArrayList<String> ids = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            ids.add(jsonArray.get(i).toString());
        }
        this.service.deleteEntities(this.user, "goals", ids);

        return Response.ok().build();
    }

    @POST
    @Path(API.weights)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @WeightListReaderInterceptor
    public Response insertWeights(List<Weight> weights) {

        this.service.insertEntities(weights);
        return Response.ok().build();
    }

    @GET
    @Path(API.weights)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @WeightListWriterInterceptor
    public Response getWeights(@Context HttpServletResponse response) {
        List<Object> missingEntities = this.service.getMissingEntities(this.user, "weights", Weight.class);

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(missingEntities).build();
    }

    @GET
    @Path(API.settings)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSettings(String data, @Context HttpServletResponse response) {
        Details details = this.settingsService.get(this.user);
        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        if(details == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok().entity(details.getSettings()).build();
        }
    }

    @POST
    @Path(API.settings)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    public Response updateSettings(Details details, @Context HttpServletResponse response) {
        this.settingsService.update(details, this.user);
        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().build();
    }

    @GET
    @Path(API.syncTimes)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastModifiedTimes() {

        ModifiedTimes times = this.service.getTimes(this.user);
        JSONObject timesJSON = new JSONObject();
        timesJSON.put("last_modified_activities", times.getLastModifiedActivities());
        timesJSON.put("last_modified_settings", times.getLastModifiedSettings());
        timesJSON.put("last_modified_goals", times.getLastModifiedGoals());
        timesJSON.put("last_modified_weights", times.getLastModifiedWeights());

        return Response.ok().entity(timesJSON).build();
    }

}
