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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Provider
@SportActivityListReaderInterceptor
public class SportActivityListReader implements ReaderInterceptor {

    private FlatbufferSerializer<SportActivityWeb> serializer;
    private UserPrincipal user;

    public SportActivityListReader() {
    }

    @Inject
    public SportActivityListReader(@SerializerQualifier(SerializerType.SportActivity) FlatbufferSerializer<SportActivityWeb> serializer,
                                   UserPrincipal user) {
        this.serializer = serializer;
        this.user = user;
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        InputStream inputStream = context.getInputStream();
        long timestamp = user.getNewServerTimestamp();
        ArrayList<SportActivityWeb> sportActivitiesWeb = serializer.deserializeArray(IOUtils.toByteArray(inputStream));
        ArrayList<SportActivity> sportActivities = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(SportActivityWeb sportActivityWeb : sportActivitiesWeb){
            sportActivities.add(helper.toSportActivity(sportActivityWeb, this.user.getId(), timestamp));
        }

        return sportActivities;
    }
}
