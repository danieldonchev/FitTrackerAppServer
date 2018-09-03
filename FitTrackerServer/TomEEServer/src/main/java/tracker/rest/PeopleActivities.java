package tracker.rest;

import com.tracker.shared.Entities.SportActivityMap;
import org.json.JSONObject;
import tracker.DAO.DaoServices.SharedActivitiesService;
import tracker.Entities.SportActivity;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("people-activities")
public class PeopleActivities {

    private SharedActivitiesService service;

    public PeopleActivities() {
    }

    @Inject
    public PeopleActivities(SharedActivitiesService service) {
        this.service = service;
    }

    @Path("activities")
    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getActivites(String data) {
        ArrayList<SportActivity> sportActivities = service.getSharedSportActivities(new JSONObject(data));
        return Response.ok().entity(sportActivities).build();
    }

    @Path("map/{activityID}/{userID}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSharedMap(@PathParam("activityID") String activityID, @PathParam("userID") String userID) {

        SportActivityMap map = service.getSportActivityMap(activityID, userID);
        return Response.ok().entity(map.serialize()).build();
    }
}