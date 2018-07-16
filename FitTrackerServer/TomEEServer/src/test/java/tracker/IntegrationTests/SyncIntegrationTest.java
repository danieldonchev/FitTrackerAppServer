package tracker.IntegrationTests;

import com.tracker.shared.Entities.GoalWeb;
import com.tracker.shared.Entities.SerializeHelper;
import com.tracker.shared.Entities.SportActivityWeb;
import org.json.JSONArray;
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

    @Test
    public void insertMissingActivities(){

        try{
            ArrayList<SportActivityWeb> sportActivityWebs = new ArrayList<>();
            SportActivityWeb sportActivityWeb1 = new SportActivityWeb(UUID.randomUUID().toString(),
                    "Running",
                    5,
                    6,
                    7,
                    8,
                    10,
                    50,
                    70);

            SportActivityWeb sportActivityWeb2 = new SportActivityWeb(UUID.randomUUID().toString(),
                    "Walking",
                    5,
                    6,
                    5,
                    8,
                    5,
                    5,
                    4);

            sportActivityWebs.add(sportActivityWeb1);
            sportActivityWebs.add(sportActivityWeb2);

        HttpsConnection httpsConnection = new HttpsConnection();
        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.syncActivities);
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.getOutputStream().write(SerializeHelper.serializeSportActivities(sportActivityWebs));

        InputStream is = connection.getInputStream();

        String receivedStr = readStream(is);
        int b = 5;


    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @Test
    public void deleteActivities(){

        try{
            JSONArray array = new JSONArray();
            array.put("58cbfeeb-8cf1-4709-9012-acb485a618ec");
            array.put("4d665982-e5a1-486e-9ce2-977dd3de5b37");

            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.deletedActivities);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(array.toString().getBytes("UTF-8"));

            InputStream is = connection.getInputStream();

            String receivedStr = readStream(is);
            int b = 5;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDeletedActivities(){
        try{
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_GET, API.deletedActivities);

            InputStream is = connection.getInputStream();

            String receivedStr = readStream(is);
            int b = 5;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
