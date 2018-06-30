package tracker.rest;

import org.json.JSONObject;
import tracker.DAO.DAOFactory;
import tracker.DAO.UserDetailsDAO;
import tracker.Markers.Secured;
import tracker.Markers.Sync;
import tracker.Entities.Users.GenericUser;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Secured
@Sync
@Path("settings")
public class Settings {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSettings(tracker.Settings settings, @Context SecurityContext context) {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);

        DAOFactory daoFactory = new DAOFactory();
        UserDetailsDAO userDetailsDAO = daoFactory.getUserDetailsDAO();
        userDetailsDAO.update(user.getId().toString(), new JSONObject(settings.getSettings()), settings.getLastModified(), user.getNewServerTimestamp());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", "settings");

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(@Context SecurityContext context, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) context.getUserPrincipal();

        DAOFactory daoFactory = new DAOFactory();
        UserDetailsDAO userDetailsDAO = daoFactory.getUserDetailsDAO();
        JSONObject settings = userDetailsDAO.getUserSettings(user.getId().toString());

        response.addHeader("Data-Type", "settings");

        return Response.ok().entity(settings.toString()).build();
    }
}
