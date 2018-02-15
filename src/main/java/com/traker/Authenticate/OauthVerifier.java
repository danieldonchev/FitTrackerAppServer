package main.java.com.traker.Authenticate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import main.java.com.traker.AppUtils;
import org.json.JSONObject;
import org.omg.CORBA.NameValuePair;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class OauthVerifier
{
    private final static String facebookAppSecret = "ce35e7d2601d6b5f198bcffd016fef67";
    private final static String facebookAppId = "262565670811889";
    private final static String fbAppAcessToken = "262565670811889|xMPAqmAHbmQs3VRycGGXBmh165w";
    private final String googleClientID =  "1044874343985-i4opmbc3kkqv7rhof9t0luehtqcqfj1c.apps.googleusercontent.com";
    private final String googleClientSecret = " -vNEYvj2goAhfmTtw8stn3ku";

    private static String getAppAccessToken()  throws IOException
    {
        URL url = new URL("https://graph.facebook.com/oauth/access_token?client_id=" + facebookAppId + "&client_secret=" + facebookAppSecret + "&grant_type=client_credentials");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        String result = AppUtils.inputStreamToString(connection.getInputStream());
        String[] data = result.split("[:,\"]");
        String string = data[4];
        return data[4];
    }

    public static boolean verifyFBToken(String tokenToInspect)
    {
        try
        {
            URL urlCheckToken = new URL("https://graph.facebook.com/debug_token?input_token=" + tokenToInspect + "&access_token=" + getAppAccessToken());
            HttpsURLConnection connection = (HttpsURLConnection) urlCheckToken.openConnection();
            connection.setRequestMethod("GET");

            JSONObject jsonObject = AppUtils.inputStreamToJson(connection.getInputStream());
            boolean isValid =  jsonObject.getJSONObject("data").getBoolean("is_valid");

            return isValid;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    public static GoogleIdToken verifyGoogleIdToken(String token) throws GeneralSecurityException, IOException
    {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(new com.google.api.client.http.javanet.NetHttpTransport(), JacksonFactory.getDefaultInstance());

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null)
        {
            return idToken;
        }
        else
        {
            return null;
        }
    }

    public static boolean CaptchaVerifier(String token){
        String secret = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe";
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(URLEncoder.encode("secret", "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(secret, "UTF-8"));
            builder.append("&");
            builder.append(URLEncoder.encode("response", "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(token, "UTF-8"));

            URL urlCheckToken = new URL("https://www.google.com/recaptcha/api/siteverify");
            HttpsURLConnection connection = (HttpsURLConnection) urlCheckToken.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(builder.toString());
            writer.flush();
            writer.close();
            os.close();
            int code = connection.getResponseCode();
            int b = 5;
            InputStream is = connection.getInputStream();
            JSONObject jsonObject = new JSONObject(AppUtils.inputStreamToString(is));
            return jsonObject.getBoolean("success");


        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
