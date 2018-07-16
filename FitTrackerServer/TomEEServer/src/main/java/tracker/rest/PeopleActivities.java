package tracker.rest;

import com.tracker.shared.Entities.SerializeHelper;
import com.tracker.shared.Entities.SportActivityMap;
import com.tracker.shared.Entities.SportActivityWithOwner;
import org.json.JSONObject;
import tracker.DAO.PreviousDAOSystem.DAOFactory;
import tracker.DAO.PreviousDAOSystem.SharedActivitiesDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("people-activities")
public class PeopleActivities {

    @Path("activities")
    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getActivites(String data) {
        DAOFactory daoFactory = new DAOFactory();
        SharedActivitiesDAO activitiesDAO = daoFactory.getSharedActivitiesDAO();
        ArrayList<SportActivityWithOwner> activityWithOwners = activitiesDAO.getSharedSportActivities(new JSONObject(data));

        return Response.ok().entity(SerializeHelper.serializeSportActivitiesWithOwners(activityWithOwners)).build();
    }

    @Path("map/{activityID}/{userID}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSharedMap(@PathParam("activityID") String activityID, @PathParam("userID") String userID) {
        DAOFactory daoFactory = new DAOFactory();
        SharedActivitiesDAO activitiesDAO = daoFactory.getSharedActivitiesDAO();
        SportActivityMap map = activitiesDAO.getSharedSportActivityMap(activityID, userID);

        return Response.ok().entity(map.serialize()).build();
    }
}
