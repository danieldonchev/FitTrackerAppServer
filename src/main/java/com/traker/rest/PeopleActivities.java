package main.java.com.traker.rest;

import com.traker.shared.SerializeHelper;
import com.traker.shared.SportActivityMap;
import com.traker.shared.SportActivityWithOwner;
import main.java.com.traker.DAO.DAOFactory;
import main.java.com.traker.DAO.SharedActivitiesDAO;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("people-activities")
public class PeopleActivities {

    @Path("activities")
    @POST
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getActivites(String data)
    {
        DAOFactory daoFactory = new DAOFactory();
        SharedActivitiesDAO activitiesDAO = daoFactory.getSharedActivitiesDAO();
        ArrayList<SportActivityWithOwner> activityWithOwners = activitiesDAO.getSharedSportActivities(new JSONObject(data));

        return Response.ok().entity(SerializeHelper.serializeSportActivitiesWithOwners(activityWithOwners)).build();
    }

    @Path("map/{activityID}/{userID}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSharedMap(@PathParam("activityID") String activityID, @PathParam("userID") String userID)
    {
        DAOFactory daoFactory = new DAOFactory();
        SharedActivitiesDAO activitiesDAO = daoFactory.getSharedActivitiesDAO();
        SportActivityMap map = activitiesDAO.getSharedSportActivityMap(activityID, userID);

        return Response.ok().entity(map.serialize()).build();
    }

    @Path("activities")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTest()
    {
        return Response.ok().entity("asdasd").build();
    }
}
