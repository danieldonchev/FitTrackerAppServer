package tracker.rest;

import org.json.JSONObject;
import tracker.Utils.API;
import tracker.DAO.DaoServices.UserSettignsService;
import tracker.Entities.Details;
import tracker.Entities.GenericUser;
import tracker.Markers.Secured;
import tracker.Markers.Sync;
import tracker.Markers.UserWriting;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Secured
@Sync
@Path(API.settings)
public class Settings {

    private UserSettignsService service;

    public Settings() {
    }

    @Inject
    public Settings(UserSettignsService service) {
        this.service = service;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @UserWriting
    public Response updateSettings(Details userSettings, @Context SecurityContext context) {
        GenericUser user = (GenericUser) context.getUserPrincipal();

        userSettings.setId(user.getId());
        this.service.update(userSettings, user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", "userSettings");

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(@Context SecurityContext context, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) context.getUserPrincipal();

        Details userSettings = this.service.get(user);
        JSONObject settingsJson = new JSONObject(userSettings.getSettings());

        response.addHeader("Data-Type", "userSettings");

        return Response.ok().entity(settingsJson.toString()).build();
    }
}
