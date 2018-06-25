package tracker.rest;

import com.tracker.shared.Entities.Weight;
import org.json.JSONObject;
import sun.misc.IOUtils;
import tracker.DAO.DAOFactory;
import tracker.DAO.WeightDAO;
import tracker.Markers.Secured;
import tracker.Markers.Sync;
import tracker.Users.GenericUser;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.io.InputStream;

@Secured
@Sync
@Path("weights")
public class Weights {

    @POST
    @Path("weight")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response insertWeight(InputStream inputStream, @Context SecurityContext context) {

        GenericUser user = (GenericUser) context.getUserPrincipal();
        user.setWriting(true);

        DAOFactory daoFactory = new DAOFactory();
        WeightDAO weightDAO = daoFactory.getWeightsDAO();
        Weight weight = null;
        try {
            weight = new Weight().deserialize(IOUtils.readFully(inputStream, -1, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        weightDAO.insertWeight(weight, user.getId().toString(), user.getNewServerTimestamp());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", Weight.class.getSimpleName());
        jsonObject.put("id", weight.date);

        return Response.ok().entity(jsonObject.toString()).build();
    }
}
