package tracker.goal;

import com.tracker.shared.entities.GoalWeb;
import org.json.JSONObject;
import tracker.goal.interceptors.GoalListWriterInterceptor;
import tracker.goal.interceptors.GoalReaderInterceptor;
import tracker.goal.interceptors.GoalWriterInterceptor;
import tracker.security.Secured;
import tracker.sync.Sync;
import tracker.sync.UserWriting;
import tracker.utils.API;
import tracker.authenticate.GenericUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.UUID;

@Stateless
@Secured
@Sync
@Path(API.goal)
public class GoalRest {

    private GenericUser user;
    private GoalService goalService;

    public GoalRest() { }

    @Inject
    public GoalRest(GoalService goalService, GenericUser user) {
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
    @GoalWriterInterceptor
    public Response getGoal(@PathParam("id") String id, @Context SecurityContext context) {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        Goal goal = this.goalService.getGoal(UUID.fromString(id), user.getId());

        return Response.ok().entity(goal).build();
    }

    @DELETE
    @Path("{id}")
    @UserWriting
    public Response deleteGoal(@PathParam("id") String id, @Context SecurityContext context) {

        goalService.deleteGoal(UUID.fromString(id), ((GenericUser) context.getUserPrincipal()).getId());

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
    public Response getGoals(@Context SecurityContext context) {
        UUID id = ((GenericUser) context.getUserPrincipal()).getId();
        List<Goal> goals = this.goalService.getGoals(id);

        return Response.ok().entity(goals).build();
    }

}
