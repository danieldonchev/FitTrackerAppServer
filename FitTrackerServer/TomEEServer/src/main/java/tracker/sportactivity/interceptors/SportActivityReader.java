package tracker.sportactivity.interceptors;

import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import org.apache.commons.io.IOUtils;
import tracker.authenticate.GenericUser;
import tracker.sportactivity.SportActivity;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.serializers.SerializerType;

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
@SportActivityReaderInterceptor
public class SportActivityReader implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;
    private FlatbufferSerializer<SportActivityWeb> serializer;

    public SportActivityReader() {
    }

    @Inject
    public SportActivityReader(@SerializerQualifier(value = SerializerType.SportActivity) FlatbufferSerializer<SportActivityWeb> serializer) {
        this.serializer = serializer;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream inputStream = context.getInputStream();
        SportActivityWeb sportActivityWeb =  serializer.deserialize(IOUtils.toByteArray(inputStream));
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        long timestamp = user.getNewServerTimestamp();

        WebEntitiesHelper helper = new WebEntitiesHelper();
        SportActivity sportActivity = helper.toSportActivity(sportActivityWeb, user, timestamp);
        return sportActivity;
    }
}
