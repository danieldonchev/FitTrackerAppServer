package tracker.rest;

import com.tracker.shared.Entities.SportActivityWeb;
import org.json.JSONArray;
import org.json.JSONObject;
import tracker.DAO.DaoServices.SynchronizationService;
import tracker.DAO.DaoServices.UserSettignsService;
import tracker.Entities.*;
import tracker.Markers.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Secured
@Sync
@Path("sync")
public class Synchronization {

    private SynchronizationService service;
    private UserSettignsService settingsService;

    public Synchronization() { }

    @Inject
    public Synchronization(SynchronizationService service, UserSettignsService settingsService) {
        this.service = service;
        this.settingsService = settingsService;
    }

    @GET
    @Path("should-sync")
    @Produces(MediaType.APPLICATION_JSON)
    public Response shouldSync(@Context HttpServletRequest req) {
        return Response.ok().build();
    }

    /*
    Gets missing activities
     */
    @GET
    @Path("sport-activities")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @SyncSportActivityListInterceptorWriter
    public Response getSportActivities(@Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        List<Object> missingEntities = this.service.getMissingEntities(user, "user_sport_activity", SportActivity.class);

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(missingEntities).build();
    }

    @POST
    @Path("sport-activities")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @SyncSportActivityListInterceptorReader
    public Response insertSportActivities(List<SportActivity> sportActivities, @Context SecurityContext securityContext) {

        this.service.insertEntities(sportActivities);

        return Response.ok().build();
    }

    @GET
    @Path("deleted-activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeletedActivities(@Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        List<String> sportActivities = this.service.getDeletedEntitiesId(user, "user_sport_activity");

        JSONArray jsonArray = new JSONArray();
        for(String id : sportActivities){
            jsonArray.put(id);
        }

        return Response.ok().entity(jsonArray.toString()).build();
    }

    @POST
    @Path("deleted-activities")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserWriting
    public Response deleteActivities(String data, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        JSONArray jsonArray = new JSONArray(data);
        ArrayList<String> ids = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            ids.add(jsonArray.get(i).toString());
        }
        this.service.deleteEntities(user, "user_sport_activity", ids);

        return Response.ok().build();
    }

    @GET
    @Path("goals")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getGoals(@Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        List<Object> missingEntities = this.service.getMissingEntities(user, "user_sport_activity", SportActivity.class);

        return Response.ok().entity(missingEntities).build();
    }

    @POST
    @Path("goals")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @SyncGoalListInterceptorReader
    public Response insertGoals(List<Goal> goals, @Context SecurityContext securityContext) {

        this.service.insertEntities(goals);
        return Response.ok().build();
    }

    @GET
    @Path("deleted-goals")
    @Produces(MediaType.APPLICATION_JSON)
    @SyncGoalListInterceptorWriter
    public Response getDeletedGoals(@Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        List<Object> missingEntities = this.service.getMissingEntities(user, "goals", Goal.class);
        return Response.ok().entity(missingEntities).build();
    }

    @POST
    @Path("deleted-goals")
    @Consumes(MediaType.APPLICATION_JSON)
    @UserWriting
    public Response deleteGoals(String data, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        JSONArray jsonArray = new JSONArray(data);
        ArrayList<String> ids = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            ids.add(jsonArray.get(i).toString());
        }
        this.service.deleteEntities(user, "goals", ids);

        return Response.ok().build();
    }

    @POST
    @Path("weights")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @SyncWeightListInterceptorReader
    public Response insertWeights(List<Weight> weights, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        this.service.insertEntities(weights);

        return Response.ok().build();
    }

    @GET
    @Path("weights")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @SyncWeightListInterceptorWriter
    public Response getWeights(@Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        List<Object> missingEntities = this.service.getMissingEntities(user, "weights", Weight.class);

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(missingEntities).build();
    }

    @GET
    @Path("settings")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSettings(String data, @Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        Details details = this.settingsService.get(user);
        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        if(details == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok().entity(details.getSettings()).build();
        }
    }

    @POST
    @Path("settings")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    public Response updateSettings(Details details, @Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        this.settingsService.update(details, user);
        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().build();
    }

    @GET
    @Path("sync-times")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastModifiedTimes(@Context SecurityContext securityContext) {

        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        ModifiedTimes times = this.service.getTimes(user);
        JSONObject timesJSON = new JSONObject();
        timesJSON.put("last_modified_activities", times.getLastModifiedActivities());
        timesJSON.put("last_modified_settings", times.getLastModifiedSettings());
        timesJSON.put("last_modified_goals", times.getLastModifiedGoals());
        timesJSON.put("last_modified_weights", times.getLastModifiedWeights());

        return Response.ok().entity(timesJSON).build();
    }

}
