package tracker.settings;

import org.json.JSONObject;
import tracker.authentication.users.UserPrincipal;
import tracker.security.Secured;
import tracker.sync.Sync;
import tracker.sync.UserWriting;
import tracker.utils.ApiConstants;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Secured
@Sync
@Path(ApiConstants.settings)
public class SettingsRest {

    private UserSettignsService service;
    private UserPrincipal user;

    public SettingsRest() {
    }

    @Inject
    public SettingsRest(UserSettignsService service, UserPrincipal user) {
        this.service = service;
        this.user = user;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @UserWriting
    public Response updateSettings(Details userSettings) {

        userSettings.setId(user.getId());
        this.service.update(userSettings, user);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", "userSettings");

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(@Context HttpServletResponse response) {

        Details userSettings = this.service.get(user);
        JSONObject settingsJson = new JSONObject(userSettings.getSettings());

        response.addHeader("Data-Type", "userSettings");

        return Response.ok().entity(settingsJson.toString()).build();
    }
}
