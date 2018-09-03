package tracker.weight;

import org.json.JSONObject;
import tracker.utils.API;
import tracker.security.Secured;
import tracker.sync.Sync;
import tracker.sync.UserWriting;
import tracker.weight.interceptors.WeightReaderInterceptor;

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
@Path(API.weight)
public class WeightRest {

    private WeightService service;

    public WeightRest() {
    }

    @Inject
    public WeightRest(WeightService service) {
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @UserWriting
    @WeightReaderInterceptor
    public Response insertWeight(Weight weight) {

        this.service.create(weight);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", Weight.class.getSimpleName());
        jsonObject.put("id", weight.getWeightKey().getDate());

        return Response.ok().entity(jsonObject.toString()).build();
    }
}
