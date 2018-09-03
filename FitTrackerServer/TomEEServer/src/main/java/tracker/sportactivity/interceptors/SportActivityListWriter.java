package tracker.sportactivity.interceptors;

import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import tracker.sportactivity.SportActivity;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.serializers.SerializerType;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@SportActivityListWriterInterceptor
public class SportActivityListWriter implements WriterInterceptor {

    @Context
    private SecurityContext securityContext;
    private FlatbufferSerializer<SportActivityWeb> serializer;

    public SportActivityListWriter() {
    }

    @Inject
    public SportActivityListWriter(@SerializerQualifier(value = SerializerType.SportActivity) FlatbufferSerializer<SportActivityWeb> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object missingEntities = context.getEntity();

        ArrayList<SportActivityWeb> sportActivities = new ArrayList<>();
        WebEntitiesHelper helper = new WebEntitiesHelper();
        for(Object sportActivity : (List<SportActivity>) missingEntities){
            sportActivities.add(helper.toSportActivityWeb((SportActivity) sportActivity));
        }

        context.getOutputStream().write(serializer.serializeArray(sportActivities));
    }
}
