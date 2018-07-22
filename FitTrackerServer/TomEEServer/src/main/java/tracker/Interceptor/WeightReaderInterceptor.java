package tracker.Interceptor;

import com.tracker.shared.Entities.WeightWeb;
import sun.misc.IOUtils;
import tracker.Entities.GenericUser;
import tracker.Entities.Weight;
import tracker.Markers.WeightInterceptorReader;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.io.InputStream;

@Provider
@WeightInterceptorReader
public class WeightReaderInterceptor implements ReaderInterceptor {

    @Context
    private SecurityContext securityContext;

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        GenericUser user = (GenericUser) securityContext.getUserPrincipal();
        InputStream is = context.getInputStream();
        WeightWeb weightWeb = new WeightWeb().deserialize(IOUtils.readFully(is, -1 , true));
        long timestamp = user.getNewServerTimestamp();
        Weight weight = new Weight(user.getId(),
                weightWeb.date,
                weightWeb.weight,
                weightWeb.lastModified,
                timestamp);


        return weight;
    }
}
