package tracker.Interceptor;

import com.tracker.shared.Entities.SerializeHelper;
import com.tracker.shared.Entities.WeightWeb;
import tracker.Entities.Weight;
import tracker.Utils.WebEntitiesHelper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@tracker.Markers.SyncWeightListInterceptorWriter
public class SyncWeightListInterceptorWriter implements WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object weightList = context.getEntity();
        ArrayList<WeightWeb> weightWebs = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(Weight weight : (List<Weight>) weightList){
            weightWebs.add(helper.toWeightWeb(weight));
        }
        context.getOutputStream().write(SerializeHelper.serializeWeights(weightWebs));
    }
}
