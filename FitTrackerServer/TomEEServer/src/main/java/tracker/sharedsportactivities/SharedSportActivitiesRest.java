package tracker.sharedsportactivities;

import org.json.JSONObject;
import tracker.sportactivity.SportActivity;
import tracker.utils.ApiConstants;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.UUID;


@Path(ApiConstants.sharedSportActivities)
public class SharedSportActivitiesRest {

    private SharedActivitiesService service;

    public SharedSportActivitiesRest() {
    }

    @Inject
    public SharedSportActivitiesRest(SharedActivitiesService service) {
        this.service = service;
    }


    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getActivites(String data) {
        ArrayList<SportActivity> sportActivities = service.getSharedSportActivities(new JSONObject(data));
        return Response.ok().entity(sportActivities).build();
    }

//    @Path(API.sportActivityMap +"/{activityID}/{userID}")
//    @GET
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response getSharedMap(@PathParam("activityID") String activityID, @PathParam("userID") String userID) {
//
//        SportActivityMap map = service.getSportActivityMap(UUID.fromString(activityID),
//                                                            UUID.fromString(userID));
//        return Response.ok().entity(map.serialize()).build();
//    }
}
