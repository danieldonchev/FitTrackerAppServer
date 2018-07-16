package tracker.Utils.Https;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.nio.charset.Charset;

import static tracker.Utils.TestUtils.readStream;
import static tracker.Utils.TestUtils.writeTokens;

public class HttpsConnection {
    public static final int HTTP_POST = 1;
    public static final int HTTP_GET = 2;
    public static final int HTTP_DELETE = 3;
    public static final int HTTP_PUT = 4;

    public static final int HTTP_MEDIA_BYTE_ARRAY = 1;
    public static final int HTTP_MEDIA_JSON = 2;

    private String androidID = "AndroidIDYes";
    private String device = "ThisIsADeviceYes";
    private String refreshToken;
    private String accessToken;

    public HttpsConnection(){
        loadTokens();
    }

    public HttpsURLConnection getConnection(int httpMethod, String urlString) throws IOException
    {
        HttpsURLConnection connection;
        HttpsClient httpsClient = new HttpsClient(urlString);
        connection = httpsClient.setUpHttpsConnection();

        checkAccessToken();

        String bearerAuth = "Bearer " + accessToken;
        long version = 0;

        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        if(httpMethod == HTTP_GET){
            connection.setRequestMethod("GET");
        } else if(httpMethod == HTTP_POST) {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
        } else if(httpMethod == HTTP_DELETE) {
            connection.setRequestMethod("DELETE");
        } else if(httpMethod == HTTP_PUT) {
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
        }

        connection.setRequestProperty("Authorization", bearerAuth);
        connection.setRequestProperty("Sync-Time", String.valueOf(version));

        return connection;
    }

    /*
    Checking the access token
    If exception is thrown then the access token has expired.
     */
    public  void checkAccessToken() {
        int i = accessToken.lastIndexOf('.') + 1;
        try{
            Jwt<Header, Claims> tokenClaims = Jwts.parser().parseClaimsJwt(accessToken.substring(0, i));
        } catch (io.jsonwebtoken.ExpiredJwtException ex){
            getAccessToken();
        }
    }

    public void getAccessToken(){
        HttpsURLConnection connection = null;
        int code = 0;
        try {
            HttpsClient httpsClient = new HttpsClient(API.accessToken);
            connection = httpsClient.setUpHttpsConnection();

            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("refresh_token", refreshToken);
                jsonObject.put("android_id", androidID);
                jsonObject.put("device", device);

                os.write(jsonObject.toString().getBytes(Charset.forName("UTF-8")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            InputStream is = connection.getInputStream();
            String accessToken = readStream(is);
            this.accessToken = accessToken;
            writeTokens(this.refreshToken, accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Constants
    {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String DEVICE = "device";
        public static final String PASSWORD = "password";
        public static final String RESPONSE_TOKEN = "responseToken";
        public static final String ACCESS_TOKEN = "accessToken";
        public static final String LOGIN_TYPE = "loginType";
    }



    public void loadTokens(){
        try {
            InputStream istream = new FileInputStream("src/test/resources/user_tokens.json");
            String jsonTxt = readStream(istream);
            JSONObject jsonObject = new JSONObject(jsonTxt);
            this.refreshToken = jsonObject.getString("refresh_token");
            this.accessToken = jsonObject.getString("access_token");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
