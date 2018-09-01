package tracker.weight.interceptors;

import com.tracker.shared.entities.WeightWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import org.apache.commons.io.IOUtils;
import tracker.authentication.users.UserPrincipal;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.serializers.SerializerType;
import tracker.weight.Weight;

import javax.inject.Inject;
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
@WeightListReaderInterceptor
public class WeightListReader implements ReaderInterceptor {

    private FlatbufferSerializer<WeightWeb> serializer;
    private UserPrincipal user;

    public WeightListReader() {
    }

    @Inject
    public WeightListReader(@SerializerQualifier(SerializerType.Weight) FlatbufferSerializer<WeightWeb> serializer,
                            UserPrincipal user) {
        this.serializer = serializer;
        this.user = user;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream is = context.getInputStream();
        ArrayList<Weight> weights = new ArrayList<>();
        ArrayList<WeightWeb> weightWebs = serializer.deserializeArray(IOUtils.toByteArray(is));
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(WeightWeb weightWeb : weightWebs){
            weights.add(helper.toWeight(weightWeb, this.user));
        }

        return weights;
    }
}
