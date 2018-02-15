package main.java.com.traker.rest;


import main.java.com.traker.DAO.DAOFactory;
import main.java.com.traker.DAO.UserDetailsDAO;
import main.java.com.traker.Markers.Secured;
import main.java.com.traker.Markers.Sync;
import main.java.com.traker.Users.GenericUser;
import org.json.JSONObject;

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
    public Response updateSettings(main.java.com.traker.Settings settings, @Context SecurityContext context)
    {
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
    public Response getSettings(@Context SecurityContext context, @Context HttpServletResponse response)
    {
        GenericUser user = (GenericUser) context.getUserPrincipal();

        DAOFactory daoFactory = new DAOFactory();
        UserDetailsDAO userDetailsDAO = daoFactory.getUserDetailsDAO();
        JSONObject settings = userDetailsDAO.getUserSettings(user.getId().toString());

        response.addHeader("Data-Type", "settings");

        return Response.ok().entity(settings.toString()).build();
    }
}
