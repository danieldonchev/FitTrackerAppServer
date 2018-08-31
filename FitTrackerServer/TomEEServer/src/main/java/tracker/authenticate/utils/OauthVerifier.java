package tracker.authenticate.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.json.JSONObject;
import tracker.utils.AppUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;

public class OauthVerifier {
    public final static String facebookAppSecret = "ce35e7d2601d6b5f198bcffd016fef67";
    public final static String facebookAppId = "262565670811889";
    public final static String fbAppAcessToken = "262565670811889|xMPAqmAHbmQs3VRycGGXBmh165w";
    public final String googleClientID = "1044874343985-i4opmbc3kkqv7rhof9t0luehtqcqfj1c.apps.googleusercontent.com";
    public final String googleClientSecret = " -vNEYvj2goAhfmTtw8stn3ku";

    private String getAppAccessToken() throws IOException {
        URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id=" + facebookAppId + "&client_secret=" + facebookAppSecret + "&grant_type=client_credentials");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        String result = AppUtils.inputStreamToString(connection.getInputStream());
        String[] data = result.split("[:,\"]");
        String string = data[4];
        return data[4];
    }

    public boolean verifyFBToken(String tokenToInspect) {
        try {
            URL urlCheckToken = new URL("https://graph.facebook.com/debug_token?input_token=" + tokenToInspect + "&access_token=" + getAppAccessToken());
            HttpsURLConnection connection = (HttpsURLConnection) urlCheckToken.openConnection();
            connection.setRequestMethod("GET");

            JSONObject jsonObject = AppUtils.inputStreamToJson(connection.getInputStream());
            boolean isValid = jsonObject.getJSONObject("data").getBoolean("is_valid");

            return isValid;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public GoogleIdToken verifyGoogleIdToken(String token) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(new com.google.api.client.http.javanet.NetHttpTransport(), JacksonFactory.getDefaultInstance());

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            return idToken;
        } else {
            return null;
        }
    }
}
