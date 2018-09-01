package tracker.goal;

import com.tracker.shared.entities.GoalWeb;
import org.json.JSONObject;
import tracker.goal.interceptors.GoalListWriterInterceptor;
import tracker.goal.interceptors.GoalReaderInterceptor;
import tracker.security.Secured;
import tracker.sync.Sync;
import tracker.sync.UserWriting;
import tracker.utils.API;
import tracker.authentication.users.UserPrincipal;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Stateless
@Secured
@Sync
@Path(API.goal)
public class GoalRest {

    private UserPrincipal user;
    private GoalService goalService;

    public GoalRest() { }

    @Inject
    public GoalRest(GoalService goalService, UserPrincipal user) {
        this.user =  user;
        this.goalService = goalService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @GoalReaderInterceptor
    public Response insertGoal(Goal goal) {

        goal = this.goalService.insertGoal(goal);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", GoalWeb.class.getSimpleName());
        jsonObject.put("id", goal.getId().toString());

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getGoal(@PathParam("id") String id) {
        Goal goal = this.goalService.getGoal(UUID.fromString(id), this.user.getId());

        return Response.ok().entity(goal).build();
    }

    @DELETE
    @Path("{id}")
    @UserWriting
    public Response deleteGoal(@PathParam("id") String id) {

        goalService.deleteGoal(UUID.fromString(id), this.user.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", GoalWeb.class.getSimpleName());
        jsonObject.put("id", id);

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @UserWriting
    @GoalReaderInterceptor
    public Response editGoal(Goal goal) {

        this.goalService.updateGoal(goal);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", GoalWeb.class.getSimpleName());
        jsonObject.put("id", goal.getId().toString());

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @GoalListWriterInterceptor
    public Response getGoals() {

        List<Goal> goals = this.goalService.getGoals(this.user.getId());

        return Response.ok().entity(goals).build();
    }

}
