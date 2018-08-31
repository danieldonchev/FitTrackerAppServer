package tracker.utils.Https;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {

        return true;
    }


//    @Override
//    public boolean verify(String hostname, SSLSession session) {
//        Log.i(TAG, "HOST NAME " + hostname);
//        if (hostname.contentEquals("XXX.XX.XXX.XXX")) {
//            Log.i(TAG, "Approving certificate for host " + hostname);
//            return true;
//        }
//        return false;
//    }
}