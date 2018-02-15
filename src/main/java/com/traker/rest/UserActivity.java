package main.java.com.traker.rest;

import com.traker.shared.SportActivity;
import main.java.com.traker.DAO.DAOFactory;
import main.java.com.traker.DAO.SportActivityDAO;
import main.java.com.traker.DAO.UserDetailsDAO;
import main.java.com.traker.Markers.Secured;
import main.java.com.traker.Markers.Sync;
import main.java.com.traker.Settings;
import main.java.com.traker.Users.GenericUser;
import org.json.JSONObject;
import sun.misc.IOUtils;

import javax.imageio.ImageIO;
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
@Path("user")
public class UserActivity
{

    @Path("sport-activity")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @POST
    public Response insertSportActivity(InputStream inputStream, @Context SecurityContext context, @Context HttpHeaders headers)
    {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);

        DAOFactory daoFactory = new DAOFactory();
        SportActivityDAO sportActivityDAO = daoFactory.getSportActivityDAO();
        SportActivity sportActivity = null;
        try {
            sportActivity = new SportActivity().deserialize(IOUtils.readFully(inputStream, -1, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sportActivityDAO.insertSportActivity(sportActivity, user.getId().toString(), user.getNewServerTimestamp());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", SportActivity.class.getSimpleName());
        jsonObject.put("id", sportActivity.getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }

    @GET
    @Path("sport-activity/{id}/{userID}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSportActivity(@PathParam("id") String id, @PathParam("userID") String userID, @Context SecurityContext context)
    {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(false);

        if(userID.equals(user.getId().toString())){
            DAOFactory daoFactory = new DAOFactory();
            SportActivityDAO sportActivityDAO = daoFactory.getSportActivityDAO();
            SportActivity sportActivity = sportActivityDAO.getSportActivity(id, userID);
            if(sportActivity == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok().entity(sportActivity.serialize()).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("sport-activity/{id}")
    public Response deleteSportActivity(@PathParam("id") String id, @Context SecurityContext context, @Context HttpServletResponse response)
    {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);

        DAOFactory factory = new DAOFactory();
        SportActivityDAO sportActivityDAO = factory.getSportActivityDAO();
        response.addHeader("Data-Type", SportActivity.class.getSimpleName());

        if(sportActivityDAO.deleteSportActivity(user.getId().toString(), id, user.getNewServerTimestamp())){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            return Response.ok().entity(jsonObject.toString()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("sport-activity")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editSportActivity(InputStream inputStream, @Context SecurityContext context) throws IOException
    {
        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);

        SportActivity sportActivity = new SportActivity().deserialize(IOUtils.readFully(inputStream, -1, true));
        DAOFactory factory = new DAOFactory();
        SportActivityDAO sportActivityDAO = factory.getSportActivityDAO();
        int result = sportActivityDAO.updateSportActivity(user.getId().toString(), sportActivity, user.getNewServerTimestamp());
        if(result == -1) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", SportActivity.class.getSimpleName());
        jsonObject.put("id", sportActivity.getId());

        return Response.ok().entity(jsonObject.toString()).build();
    }



    @POST
    @Path("profile-pic")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response postProfilePic(InputStream inputStream, @Context SecurityContext context){

        GenericUser user = (GenericUser) context.getUserPrincipal();
        try {
            File outputFile = new File("D:/server_pics/" + user.getId().toString() + ".png");
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
