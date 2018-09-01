package tracker.weight.interceptors;

import com.tracker.shared.entities.WeightWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import org.apache.commons.io.IOUtils;
import tracker.authentication.users.UserPrincipal;
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

@Provider
@WeightReaderInterceptor
public class WeightReader implements ReaderInterceptor {

    private FlatbufferSerializer<WeightWeb> serializer;
    private UserPrincipal user;

    public WeightReader() {
    }

    @Inject
    public WeightReader(@SerializerQualifier(SerializerType.Weight) FlatbufferSerializer<WeightWeb> serializer,
                        UserPrincipal user) {
        this.serializer = serializer;
        this.user = user;
    }


    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream is = context.getInputStream();
        WeightWeb weightWeb = new WeightWeb().deserialize(IOUtils.toByteArray(is));
        long timestamp = this.user.getNewServerTimestamp();
        Weight weight = new Weight(this.user.getId(),
                weightWeb.getDate(),
                weightWeb.getWeight(),
                weightWeb.getLastModified(),
                timestamp);

        return weight;
    }
}
