package tracker.rest;

import com.tracker.shared.Entities.GoalWeb;
import org.json.JSONObject;
import tracker.DAO.DAOServices.GoalService;
import tracker.Entities.Goal;
import tracker.Markers.GoalInterceptor;
import tracker.Markers.Secured;
import tracker.Markers.Sync;
import tracker.Markers.UserWriting;
import tracker.Users.GenericUser;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Stateless
@Secured
@Sync
@Path("goals")
public class Goals {

    private GoalService goalService;

    public Goals() { }

    @Inject
    public Goals(GoalService goalService) {
       this.goalService = goalService;
    }

    @POST
    @Path("goal")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @GoalInterceptor
    public Response insertGoal(Goal goal) {

        goal = this.goalService.insertGoal(goal);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", GoalWeb.class.getSimpleName());
        jsonObject.put("id", goal.getGoalKey().getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @DELETE
    @Path("goal/{id}")
    @UserWriting
    @GoalInterceptor
    public Response deleteGoal(@PathParam("id") String id, @Context SecurityContext context) {

        goalService.deleteGoal(id, ((GenericUser) context.getUserPrincipal()).getId().toString());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", GoalWeb.class.getSimpleName());
        jsonObject.put("id", id);

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @PUT
    @Path("goal")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @UserWriting
    @GoalInterceptor
    public Response editGoal(Goal goal) {

        this.goalService.updateGoal(goal);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", GoalWeb.class.getSimpleName());
        jsonObject.put("id", goal.getGoalKey().getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }
}
