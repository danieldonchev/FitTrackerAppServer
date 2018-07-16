package tracker.Interceptor;

import com.tracker.shared.Entities.GoalWeb;
import com.tracker.shared.Entities.SerializeHelper;
import com.tracker.shared.Entities.WeightWeb;
import sun.misc.IOUtils;
import tracker.Entities.Goal;
import tracker.Entities.Users.GenericUser;
import tracker.Entities.Weight;
import tracker.WebEntitiesHelper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Provider
@tracker.Markers.SyncWeightListInterceptorReader
public class SyncWeightListInterceptorReader implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        GenericUser user = (GenericUser)  securityContext.getUserPrincipal();
        InputStream is = context.getInputStream();
        ArrayList<Weight> weights = new ArrayList<>();
        ArrayList<WeightWeb> weightWebs = SerializeHelper.deserializeWeights(IOUtils.readFully(is, -1, true));
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(WeightWeb weightWeb : weightWebs){
            weights.add(helper.toWeight(weightWeb, user));
        }

        return weights;
    }
}