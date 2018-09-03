package tracker.sportactivity.interceptors;

import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import tracker.sportactivity.SportActivity;
import tracker.utils.WebEntitiesHelper;
import tracker.utils.serializers.SerializerQualifier;
import tracker.utils.serializers.SerializerType;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

@Provider
@SportActivityWriterInterceptor
public class SportActivityWriter implements WriterInterceptor {

    private FlatbufferSerializer<SportActivityWeb> serializer;

    public SportActivityWriter() {
    }

    @Inject
    public SportActivityWriter(@SerializerQualifier(value = SerializerType.SportActivity) FlatbufferSerializer<SportActivityWeb> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        SportActivity sportActivity = (SportActivity) context.getEntity();

        WebEntitiesHelper helper = new WebEntitiesHelper();
        SportActivityWeb sportActivityWeb = helper.toSportActivityWeb(sportActivity);
        context.getOutputStream().write(serializer.serialize(sportActivityWeb));

    }
}
