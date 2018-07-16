package tracker.IntegrationTests;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import tracker.Utils.Https.API;
import tracker.Utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;

import static tracker.Utils.Https.HttpsConnection.HTTP_PUT;
import static tracker.Utils.TestUtils.readStream;

public class UserSettingsIntegrationTest {

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
            // GoalWeb goalWebReceived = new GoalWeb().deserialize(IOUtils.readFully(is, -1, true));
            String receivedStr = readStream(is);
            int b = 5;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
