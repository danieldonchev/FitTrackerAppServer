package tracker.IntegrationTests;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import tracker.Https.API;
import tracker.Https.HttpsClient;
import tracker.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;

import static tracker.Utils.TestUtils.readStream;
import static tracker.Utils.TestUtils.writeTokens;

public class UserAuthIntegrationTest {

    @Test
    public void register() throws IOException {
        JSONObject jsonObject = getAccDetails();
        HttpsURLConnection connection = sendAccDetails(jsonObject);
        Assert.assertNotNull(connection);
        Assert.assertEquals(connection.getResponseCode(), 200);
    }

    @Test
    public void registerExistingAccount() throws IOException{
        JSONObject jsonObject = getAccDetails();
        HttpsURLConnection connection = sendAccDetails(jsonObject);
        Assert.assertNotNull(connection);
        Assert.assertEquals(connection.getResponseCode(), 409);
    }

    @Test
    public void signIn(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(HttpsConnection.Constants.EMAIL, "didone7@abv.bg");
        jsonObject.put(HttpsConnection.Constants.PASSWORD, "123456");
        jsonObject.put(HttpsConnection.Constants.DEVICE, "LG-G3");

        try {
            HttpsURLConnection connection;
            HttpsClient httpsClient = new HttpsClient(API.localLogin);
            connection = httpsClient.setUpHttpsConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.getOutputStream().write(jsonObject.toString().getBytes(Charset.forName("UTF-8")));
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String receivedStr = readStream(in);
            JSONObject receivedObject = new JSONObject(receivedStr);
            String refreshToken = receivedObject.getString("refresh_token");
            String accessToken = receivedObject.getString("access_token");
            writeTokens(receivedObject.getString("refresh_token"),
                    receivedObject.getString("access_token"));
            int b = 5;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void forgottenPasswordTest(){
        try {
            String email = "didone7@abv.bg";

            HttpsURLConnection connection;
            HttpsClient httpsClient = new HttpsClient(API.passwordToken);
            connection = httpsClient.setUpHttpsConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.getOutputStream().write(email.getBytes(Charset.forName("UTF-8")));
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String receivedStr = readStream(in);
            JSONObject receivedObject = new JSONObject(receivedStr);
            String refreshToken = receivedObject.getString("refresh_token");
            String accessToken = receivedObject.getString("access_token");
            int b = 5;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void changePassword(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", "didone7@abv.bg");
            jsonObject.put("password", "123456");
            jsonObject.put("code", "1R7L9SNX");

            HttpsURLConnection connection;
            HttpsClient httpsClient = new HttpsClient(API.changePassword);
            connection = httpsClient.setUpHttpsConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.getOutputStream().write(jsonObject.toString().getBytes(Charset.forName("UTF-8")));
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String receivedStr = readStream(in);
            JSONObject receivedObject = new JSONObject(receivedStr);
            String refreshToken = receivedObject.getString("refresh_token");
            String accessToken = receivedObject.getString("access_token");
            int b = 5;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAccessTokenFromRefreshToken(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("refresh_token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaXNzIjoidHJhY2tlci5jb20iLCJpYXQiOjE1MzAzMTc3NDcsImV4cCI6MTUzODA5Mzc0NywiZW1haWwiOiJkaWRvbmU3QGFidi5iZyIsInVzZXJJRCI6ImI2OWY2MDM3LWU5NTktNGYzOS04OGU5LWJlY2RjOTNiMjk5NSJ9.FXGYXgUYY7wxl6f2447yH-gR_ZurybybiqVDNTHmYBU");

            HttpsURLConnection connection;
            HttpsClient httpsClient = new HttpsClient(API.accessToken);
            connection = httpsClient.setUpHttpsConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.getOutputStream().write(jsonObject.toString().getBytes(Charset.forName("UTF-8")));
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String accessToken = readStream(in);

            Assert.assertEquals(connection.getResponseCode(), 200);
            Assert.assertNotNull(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getAccDetails(){
        return new JSONObject()
        .put(HttpsConnection.Constants.EMAIL, "didone7@abv.bg")
        .put(HttpsConnection.Constants.NAME, "Daniel Donchev")
        .put(HttpsConnection.Constants.PASSWORD, "123456")
        .put(HttpsConnection.Constants.DEVICE, "LG-G3");
    }

    private HttpsURLConnection sendAccDetails(JSONObject details){
        try {
            HttpsURLConnection connection;
            HttpsClient httpsClient = new HttpsClient(API.register);
            connection = httpsClient.setUpHttpsConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            connection.getOutputStream().write(details.toString().getBytes(Charset.forName("UTF-8")));
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String receivedStr = readStream(in);
            JSONObject receivedObject = new JSONObject(receivedStr);
            writeTokens(receivedObject.getString("refresh_token"),
                    receivedObject.getString("access_token"));
            return connection;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
