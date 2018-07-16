package tracker.rest;

import org.json.JSONObject;
import tracker.DAO.DAOServices.WeightService;
import tracker.Entities.Weight;
import tracker.Markers.Secured;
import tracker.Markers.Sync;
import tracker.Markers.UserWriting;
import tracker.Markers.WeightInterceptorReader;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Secured
@Sync
@Path("weights")
public class Weights {

    private WeightService service;

    public Weights() {
    }

    @Inject
    public Weights(WeightService service) {
        this.service = service;
    }

    @POST
    @Path("weight")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @WeightInterceptorReader
    public Response insertWeight(Weight weight, @Context SecurityContext context) {

        this.service.create(weight);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", Weight.class.getSimpleName());
        jsonObject.put("id", weight.getWeightKey().getDate());

        return Response.ok().entity(jsonObject.toString()).build();
    }
}
