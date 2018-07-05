package tracker.IntegrationTests;

import com.tracker.shared.Entities.*;
import org.junit.jupiter.api.Test;
import sun.misc.IOUtils;
import tracker.Https.API;
import tracker.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static tracker.Https.HttpsConnection.HTTP_POST;
import static tracker.Utils.TestUtils.readStream;

public class WeightIntegrationTest {

    @Test
    public void insertWeight(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.weight);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            WeightWeb weightWeb = new WeightWeb(80, 12323512, 3472623);
            InputStream ist = new ByteArrayInputStream(weightWeb.serialize());
            WeightWeb test = new WeightWeb().deserialize(IOUtils.readFully(ist, -1 , true));

            connection.getOutputStream().write(weightWeb.serialize());
            InputStream is = connection.getInputStream();
            // GoalWeb goalWebReceived = new GoalWeb().deserialize(IOUtils.readFully(is, -1, true));
            String receivedStr = readStream(is);
            int b = 5;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
