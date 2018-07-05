package tracker.IntegrationTests;

import com.tracker.shared.Entities.GoalWeb;
import org.junit.jupiter.api.Test;
import sun.misc.IOUtils;
import tracker.Https.API;
import tracker.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;

import static tracker.Https.HttpsConnection.HTTP_GET;
import static tracker.Utils.TestUtils.readStream;

public class SyncIntegrationTest {

    @Test
    public void getMissingActivitiesTest(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_GET, API.syncActivities);
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            InputStream is = connection.getInputStream();
            GoalWeb goalWebReceived = new GoalWeb().deserialize(IOUtils.readFully(is, -1, true));
            String receivedStr = readStream(is);
            int b = 5;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
