package tracker.IntegrationTests;

import com.tracker.shared.Entities.*;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import sun.misc.IOUtils;
import tracker.Utils.Https.API;
import tracker.Utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static tracker.Utils.Https.HttpsConnection.HTTP_GET;
import static tracker.Utils.Https.HttpsConnection.HTTP_POST;
import static tracker.Utils.Https.HttpsConnection.HTTP_PUT;
import static tracker.Utils.TestUtils.readStream;

public class UserActivityIntegrationTest {

    @Test
    public void insertSportActivity(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.sportActivity);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            ArrayList<SplitWeb> splitWebs = new ArrayList<>();
            splitWebs.add(new SplitWeb(1, 5, 5));
            splitWebs.add(new SplitWeb(2, 6, 6));
            SportActivityWeb sportActivityWeb = new SportActivityWeb(UUID.randomUUID().toString(),
                    "Running",
                    5,
                    6,
                    7,
                    8,
                    10,
                    50,
                    70);
            sportActivityWeb.setSplitWebs(splitWebs);
            SportActivityMap sportActivityMap = new SportActivityMap();
            ArrayList<LatLng> markers = new ArrayList<>();
            markers.add(new LatLng(5, 6));
            markers.add(new LatLng(7, 8));
            sportActivityMap.setMarkers(markers);
            sportActivityWeb.setSportActivityMap(sportActivityMap);


            connection.getOutputStream().write(sportActivityWeb.serialize());
            InputStream is = connection.getInputStream();
            String receivedStr = readStream(is);
            JSONObject object = new JSONObject(receivedStr);
            Assert.assertNotNull(connection);
            Assert.assertEquals(connection.getResponseCode(), 200);
            Assert.assertEquals(object.getString("id"), sportActivityWeb.getId());
            Assert.assertEquals(object.getString("data"), GoalWeb.class.getSimpleName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSportActivity(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_GET, API.sportActivity + "/469b4f22-09d3-40a5-ab65-33b81c45e573" + "/b69f6037-e959-4f39-88e9-becdc93b2995");
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            InputStream is = connection.getInputStream();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateSportActivity(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_PUT, API.sportActivity);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            ArrayList<SplitWeb> splitWebs = new ArrayList<>();
            splitWebs.add(new SplitWeb(1, 5, 5));
            splitWebs.add(new SplitWeb(2, 7, 7));
            SportActivityWeb sportActivityWeb = new SportActivityWeb("469b4f22-09d3-40a5-ab65-33b81c45e573",
                    "Running",
                    5,
                    6,
                    7000,
                    800,
                    200,
                    300,
                    70);
            sportActivityWeb.setSplitWebs(splitWebs);
            SportActivityMap sportActivityMap = new SportActivityMap();
            ArrayList<LatLng> markers = new ArrayList<>();
            markers.add(new LatLng(5, 6));
            markers.add(new LatLng(7, 8));
            sportActivityMap.setMarkers(markers);
            sportActivityWeb.setSportActivityMap(sportActivityMap);


            connection.getOutputStream().write(sportActivityWeb.serialize());
            InputStream is = connection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
