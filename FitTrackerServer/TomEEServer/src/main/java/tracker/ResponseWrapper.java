package tracker;

import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ResponseWrapper  {

    public ResponseWrapper() {

    }

    public Response getResponseOk(){
        return Response.ok().build();
    }
}
