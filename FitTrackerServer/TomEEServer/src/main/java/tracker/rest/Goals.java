package tracker.rest;

import com.tracker.shared.Entities.GoalWeb;
import org.json.JSONObject;
import tracker.API;
import tracker.DAO.DaoServices.GoalService;
import tracker.Entities.GenericUser;
import tracker.Entities.Goal;
import tracker.Markers.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Stateless
@Secured
@Sync
@Path(API.goal)
public class Goals {

    private GoalService goalService;

    public Goals() { }

    @Inject
    public Goals(GoalService goalService) {
       this.goalService = goalService;
    }

    @POST
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
    @Path("{id}")
    @UserWriting
    @GoalInterceptor
    public Response deleteGoal(@PathParam("id") String id, @Context SecurityContext context) {

        goalService.deleteGoal(id, ((GenericUser) context.getUserPrincipal()).getId());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", GoalWeb.class.getSimpleName());
        jsonObject.put("id", id);

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @PUT
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

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GoalListInterceptorWriter
    public Response getGoals(@Context SecurityContext context) {
        String id = ((GenericUser) context.getUserPrincipal()).getId();
        List<Goal> goals = this.goalService.getGoals(id);

        return Response.ok().entity(goals).build();
    }

}
