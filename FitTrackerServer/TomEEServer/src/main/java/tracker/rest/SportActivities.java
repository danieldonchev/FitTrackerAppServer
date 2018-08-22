package tracker.rest;

import com.tracker.shared.Entities.SportActivityWeb;
import org.json.JSONObject;
import tracker.API;
import tracker.DAO.DaoServices.SportActivityService;
import tracker.DAO.DaoServices.SportActivityServiceImpl;
import tracker.Entities.GenericUser;
import tracker.Entities.SportActivity;
import tracker.Markers.*;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Secured
@Sync
@Path(API.sportActivity)
public class SportActivities {

    private SportActivityService service;

    public SportActivities(){}

    @Inject
    public SportActivities(SportActivityService service){
        this.service = service;
    }

    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @POST
    @UserWriting
    @SportActivityInterceptorReader
    public Response insertSportActivity(SportActivity sportActivity, @Context SecurityContext context, @Context HttpHeaders headers) {

        this.service.create(sportActivity);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", SportActivityWeb.class.getSimpleName());
        jsonObject.put("id", sportActivity.getSportActivityKey().getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @GET
    @Path("{id}/{userID}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @SportActivityInterceptorWriter
    public Response getSportActivity(@PathParam("id") String id, @PathParam("userID") String userID, @Context SecurityContext context) {
        GenericUser user = (GenericUser) context.getUserPrincipal();

        if (userID.equals(user.getId())) {
            SportActivity sportActivity = this.service.read(id, userID);
            if (sportActivity == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok().entity(sportActivity).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("{id}")
    @UserWriting
    public Response deleteSportActivity(@PathParam("id") String id, @Context SecurityContext context, @Context HttpServletResponse response) {
        GenericUser user = (GenericUser) context.getUserPrincipal();

        response.addHeader("Data-Type", SportActivityWeb.class.getSimpleName());
        this.service.delete(id, user.getId());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return Response.ok().entity(jsonObject.toString()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @UserWriting
    @SportActivityInterceptorReader
    public Response editSportActivity(SportActivity sportActivity, @Context SecurityContext context) {

        this.service.update(sportActivity);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", SportActivityWeb.class.getSimpleName());
        jsonObject.put("id", sportActivity.getSportActivityKey().getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @POST
    @Path("profile-pic")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response postProfilePic(InputStream inputStream, @Context SecurityContext context) {

        GenericUser user = (GenericUser) context.getUserPrincipal();
        try {
            File outputFile = new File("D:/server_pics/" + user.getId()+ ".png");
            BufferedImage image = ImageIO.read(inputStream);
            BufferedImage dimg = new BufferedImage(120, 120, image.getType());
            Graphics2D g = dimg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, 120, 120, 0, 0, image.getWidth(), image.getHeight(), null);
            g.dispose();
            ImageIO.write(dimg, "png", outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().build();
    }

}
