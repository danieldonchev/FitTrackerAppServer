package tracker.IntegrationTests;

import com.tracker.shared.entities.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import tracker.utils.Https.API;
import tracker.utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static tracker.utils.Https.HttpsConnection.HTTP_POST;

public class WeightIntegrationTest {

    @Test
    public void insertWeight(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.weight);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            WeightWeb weightWeb = new WeightWeb(80, 12323512, 3472623);
            InputStream inputStream = new ByteArrayInputStream(weightWeb.serialize());
            WeightWeb test = new WeightWeb().deserialize(IOUtils.toByteArray(inputStream));

            connection.getOutputStream().write(weightWeb.serialize());
            InputStream is = connection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
