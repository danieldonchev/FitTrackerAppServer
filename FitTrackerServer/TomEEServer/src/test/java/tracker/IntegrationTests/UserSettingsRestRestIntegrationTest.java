package tracker.IntegrationTests;

import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import tracker.utils.Https.API;
import tracker.utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;

import static tracker.utils.Https.HttpsConnection.HTTP_PUT;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserSettingsRestRestIntegrationTest {

    @Test
    public void getSettings(){

    }

    @Test
    public void updateSettings(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_PUT, API.settings);
            connection.setRequestProperty("Content-Type", "application/json");

            JSONObject jsonObject = new JSONObject();
            JSONObject settings = new JSONObject();
            settings.put("split", 500);
            jsonObject.put("settings", settings.toString());
            jsonObject.put("lastModified", 123461);
            connection.getOutputStream().write(jsonObject.toString().getBytes("UTF-8"));
            InputStream is = connection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
