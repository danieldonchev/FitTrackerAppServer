package main.java.com.traker.rest;

import com.traker.shared.Goal;
import main.java.com.traker.DAO.DAOFactory;
import main.java.com.traker.DAO.GoalDAO;
import main.java.com.traker.Markers.Secured;
import main.java.com.traker.Markers.Sync;
import main.java.com.traker.Users.GenericUser;
import org.json.JSONObject;
import sun.misc.IOUtils;


import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;

@Secured
@Sync
@Path("goals")
public class Goals {

    @POST
    @Path("goal")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response insertGoal(InputStream inputStream, @Context SecurityContext context){

        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);

        DAOFactory daoFactory = new DAOFactory();
        GoalDAO goalDAO = daoFactory.getGoalDAO();
        Goal goal = null;
        try {
            goal = new Goal().deserialize(IOUtils.readFully(inputStream, -1, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        goalDAO.insertGoal(goal, user.getId().toString(), user.getNewServerTimestamp());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", Goal.class.getSimpleName());
        jsonObject.put("id", goal.getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @DELETE
    @Path("goal/{id}")
    public Response deleteGoal(@PathParam("id") String id, @Context SecurityContext context, @Context HttpServletResponse response)
    {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);


        DAOFactory factory = new DAOFactory();
        GoalDAO goalDAO = factory.getGoalDAO();
        response.addHeader("Data-Type", Goal.class.getSimpleName());

        goalDAO.deleteGoal(user.getId().toString(), id, user.getNewServerTimestamp());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            return Response.ok().entity(jsonObject.toString()).build();

    }

    @PUT
    @Path("goal")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editGoal(InputStream inputStream, @Context SecurityContext context) throws IOException
    {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);

        Goal goal = new Goal().deserialize(IOUtils.readFully(inputStream, -1, true));
        DAOFactory factory = new DAOFactory();
        GoalDAO goalDAO = factory.getGoalDAO();
        int result = goalDAO.updateGoal(goal, user.getId().toString(), user.getNewServerTimestamp());
        if(result == -1) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", Goal.class.getSimpleName());
        jsonObject.put("id", goal.getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }
}
