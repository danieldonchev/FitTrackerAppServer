package tracker.weight.interceptors;

import com.tracker.shared.entities.WeightWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.serializers.SerializerType;
import tracker.weight.Weight;
import tracker.utils.WebEntitiesHelper;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@WeightListWriterInterceptor
public class WeightListWriter implements WriterInterceptor {

    private FlatbufferSerializer<WeightWeb> serializer;

    public WeightListWriter() {
    }

    @Inject
    public WeightListWriter(@SerializerQualifier(SerializerType.Weight) FlatbufferSerializer<WeightWeb> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object weightList = context.getEntity();
        ArrayList<WeightWeb> weightWebs = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(Weight weight : (List<Weight>) weightList){
            weightWebs.add(helper.toWeightWeb(weight));
        }
        context.getOutputStream().write(serializer.serializeArray(weightWebs));
    }
}
