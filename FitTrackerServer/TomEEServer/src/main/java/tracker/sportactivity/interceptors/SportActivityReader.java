package tracker.sportactivity.interceptors;

import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import org.apache.commons.io.IOUtils;
import tracker.authentication.users.UserPrincipal;
import tracker.sportactivity.SportActivity;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.serializers.SerializerType;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;

@Provider
@SportActivityReaderInterceptor
public class SportActivityReader implements ReaderInterceptor {

    private FlatbufferSerializer<SportActivityWeb> serializer;
    private UserPrincipal user;

    public SportActivityReader() {
    }

    @Inject
    public SportActivityReader(@SerializerQualifier(value = SerializerType.SportActivity) FlatbufferSerializer<SportActivityWeb> serializer,
                               UserPrincipal user) {
        this.serializer = serializer;
        this.user = user;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream inputStream = context.getInputStream();
        SportActivityWeb sportActivityWeb =  serializer.deserialize(IOUtils.toByteArray(inputStream));

        long timestamp = this.user.getNewServerTimestamp();

        WebEntitiesHelper helper = new WebEntitiesHelper();
        SportActivity sportActivity = helper.toSportActivity(sportActivityWeb, user.getId(), timestamp);
        return sportActivity;
    }
}
