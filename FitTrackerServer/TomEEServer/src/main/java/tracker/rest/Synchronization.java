package tracker.rest;

import com.tracker.shared.Entities.GoalWeb;
import com.tracker.shared.Entities.SerializeHelper;
import com.tracker.shared.Entities.SportActivityWeb;
import com.tracker.shared.Entities.WeightWeb;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.IOUtils;
import tracker.DAO.*;
import tracker.DAO.DAOServices.SynchronizationService;
import tracker.Entities.SportActivity;
import tracker.Markers.Secured;
import tracker.Markers.Sync;
import tracker.Entities.Users.GenericUser;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Secured
@Sync
@Path("sync")
public class Synchronization {

    private SynchronizationService service;

    public Synchronization() {
    }

    @Inject
    public Synchronization(SynchronizationService service) {
        this.service = service;
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
    public Response getSportActivities(@Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        List<Object> sportact = this.service.getMissingEntities(user, "user_sport_activity", SportActivity.class);
//        for(Object sportActivity : sportact){
//            ((SportActivity) sportActivity).
//        }

        DAOFactory daoFactory = new DAOFactory();
        SportActivityDAO sportActivityDAO = daoFactory.getSportActivityDAO();

        String where = SportActivityDAOImpl.Constants.SPORT_ACTIVITY_LAST_SYNC + ">? AND " +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_USERID + "=? AND " +
                SportActivityDAOImpl.Constants.SPORT_ACTIVITY_DELETED + "=0;";
        Object[] args = {user.getClientSyncTimestamp()};

        ArrayList<SportActivityWeb> sportActivities = sportActivityDAO.getActivities(user.getId(), where, args, null, 0);


        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(SerializeHelper.serializeSportActivities(sportActivities)).build();
    }

    @POST
    @Path("sport-activities")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response insertSportActivities(InputStream inputStream, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        user.setWriting(true);
        long timestamp = user.getNewServerTimestamp();

        DAOFactory daoFactory = new DAOFactory();
        SportActivityDAO sportActivityDAO = daoFactory.getSportActivityDAO();
        try {
            List<SportActivityWeb> sportActivities = SerializeHelper.deserializeSportActivities(IOUtils.readFully(inputStream, -1, true));
            for (SportActivityWeb sportActivityWeb : sportActivities) {
                sportActivityDAO.insertSportActivity(sportActivityWeb, user.getId().toString(), timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

    @GET
    @Path("deleted-activities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeletedActivities(@Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        DAOFactory daoFactory = new DAOFactory();
        SportActivityDAO sportActivityDAO = daoFactory.getSportActivityDAO();
        JSONArray jsonArray = sportActivityDAO.getDeletedSportActivities(user.getId().toString());

        return Response.ok().entity(jsonArray.toString()).build();
    }

    @POST
    @Path("deleted-activities")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteActivities(String data, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        user.setWriting(true);
        JSONArray jsonArray = new JSONArray(data);
        DAOFactory daoFactory = new DAOFactory();
        SportActivityDAO sportActivityDAO = daoFactory.getSportActivityDAO();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            sportActivityDAO.deleteSportActivity(user.getId().toString(), jsonObject.getString("id"), user.getNewServerTimestamp());
        }

        return Response.ok().build();
    }

    @GET
    @Path("goals")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getGoals(@Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        DAOFactory daoFactory = new DAOFactory();
        GoalDAO goalDAO = daoFactory.getGoalDAO();

        String where = GoalDAOImpl.Constants.LAST_SYNC + ">? AND " +
                GoalDAOImpl.Constants.USERID + "=? AND " +
                GoalDAOImpl.Constants.DELETED + "=0;";
        Object[] args = {user.getClientSyncTimestamp()};

        ArrayList<GoalWeb> goalWebs = goalDAO.getGoals(user.getId(), where, args, null, 0);

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(SerializeHelper.serializeGoals(goalWebs)).build();
    }

    @POST
    @Path("goals")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response insertGoals(InputStream inputStream, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        user.setWriting(true);
        long timestamp = user.getNewServerTimestamp();

        DAOFactory daoFactory = new DAOFactory();
        GoalDAO goalDAO = daoFactory.getGoalDAO();
        try {
            ArrayList<GoalWeb> goalWebs = SerializeHelper.deserializeGoals(IOUtils.readFully(inputStream, -1, true));
            for (GoalWeb goalWeb : goalWebs) {
                try {
                    goalDAO.insertGoal(goalWeb, user.getId().toString(), timestamp);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

    @GET
    @Path("deleted-goals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeletedGoals(@Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        DAOFactory daoFactory = new DAOFactory();
        GoalDAO goalDAO = daoFactory.getGoalDAO();
        JSONArray jsonArray = goalDAO.getDeletedGoals(user.getId().toString());

        return Response.ok().entity(jsonArray.toString()).build();
    }

    @POST
    @Path("deleted-goals")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteGoals(String data, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        user.setWriting(true);
        JSONArray jsonArray = new JSONArray(data);
        DAOFactory daoFactory = new DAOFactory();
        GoalDAO goalDAO = daoFactory.getGoalDAO();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                goalDAO.deleteGoal(user.getId().toString(), jsonArray.getString(i), user.getNewServerTimestamp());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        return Response.ok().build();
    }

    @POST
    @Path("weights")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response insertWeights(InputStream inputStream, @Context SecurityContext securityContext) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        user.setWriting(true);
        long timestamp = user.getNewServerTimestamp();

        DAOFactory daoFactory = new DAOFactory();
        WeightDAO weightDAO = daoFactory.getWeightsDAO();
        try {
            ArrayList<WeightWeb> weightWebs = SerializeHelper.deserializeWeights(IOUtils.readFully(inputStream, -1, true));
            for (WeightWeb weightWeb : weightWebs) {
                weightDAO.insertWeight(weightWeb, user.getId(), timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

    @GET
    @Path("weights")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getWeights(@Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        DAOFactory daoFactory = new DAOFactory();
        WeightDAO weightDAO = daoFactory.getWeightsDAO();

        String where = WeightsDAOImpl.Constants.LAST_SYNC + ">? AND " +
                WeightsDAOImpl.Constants.USER_ID + "=?";
        Object[] args = {user.getClientSyncTimestamp()};

        ArrayList<WeightWeb> weightWebs = weightDAO.getWeights(user.getId(), where, args, null, 0);

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(SerializeHelper.serializeWeights(weightWebs)).build();
    }

    @GET
    @Path("settings")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSettings(String data, @Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();

        DAOFactory daoFactory = new DAOFactory();
        UserDetailsDAO detailsDAO = daoFactory.getUserDetailsDAO();

        String where = UserDetailsDAOImpl.Constants.COLUMN_LAST_SYNC + ">? AND " +
                UserDetailsDAOImpl.Constants.COLUMN_ID + "=?";
        Object[] args = {user.getClientSyncTimestamp(), user.getId()};

        JSONObject object = detailsDAO.getUserSettings(user.getId(), where, args);

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().entity(object.toString()).build();
    }

    @POST
    @Path("settings")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response updateSettings(tracker.Settings settings, @Context SecurityContext securityContext, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        user.setWriting(true);
        DAOFactory daoFactory = new DAOFactory();
        UserDetailsDAO detailsDAO = daoFactory.getUserDetailsDAO();

        detailsDAO.update(user.getId().toString(), new JSONObject(settings.getSettings()), settings.getLastModified(),
                user.getNewServerTimestamp());

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        return Response.ok().build();
    }

    @GET
    @Path("sync-times")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLastModifiedTimes(@Context SecurityContext securityContext) {

        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        DAOFactory daoFactory = new DAOFactory();
        UserSyncDAO userSyncDAO = daoFactory.getUserSyncDAO();
        JSONObject jsonObject = userSyncDAO.getLastModifiedTimes(user.getId().toString());

        return Response.ok().entity(jsonObject.toString()).build();
    }

}
