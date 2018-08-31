package tracker.IntegrationTests;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.serializers.GoalSerializer;
import com.tracker.shared.serializers.SportActivitySerializer;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import tracker.utils.Https.API;
import tracker.utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static tracker.utils.Https.HttpsConnection.HTTP_GET;
import static tracker.utils.Https.HttpsConnection.HTTP_POST;

public class SyncIntegrationTest {

    @Test
    public void getMissingActivitiesTest(){
        try {
            SportActivitySerializer serializer = new SportActivitySerializer();
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_GET, API.syncActivities);
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            InputStream is = connection.getInputStream();
            ArrayList<SportActivityWeb> sportActivityWebs = serializer.deserializeArray(IOUtils.toByteArray(is));

            Assert.assertEquals(connection.getResponseCode(), 200);
            Assert.assertNotNull(sportActivityWebs);

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
            SportActivitySerializer serializer = new SportActivitySerializer();
        HttpsConnection httpsConnection = new HttpsConnection();
        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.syncActivities);
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.getOutputStream().write(serializer.serializeArray(sportActivityWebs));

        InputStream is = connection.getInputStream();
        Assert.assertEquals(connection.getResponseCode(), 200);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertMissingGoals(){
        try{
            ArrayList<GoalWeb> goalWebs = new ArrayList<>();
            GoalWeb goal = new GoalWeb(UUID.fromString("dc9e60b2-5f0d-4a94-9226-c76817bfd611"),
                    1,
                    555.42d,
                    180L,
                    50L,
                    300L,
                    1L,
                    2L,
                    1L);


            GoalWeb goal2 = new GoalWeb(UUID.fromString("dc9e60b2-5f0d-4a94-9226-c76817bfd612"),
                    1,
                    555.42d,
                    180L,
                    50L,
                    300L,
                    1L,
                    2L,
                    1L);


            goalWebs.add(goal);
            goalWebs.add(goal2);

            GoalSerializer serializer = new GoalSerializer();
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.missingGoals);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.getOutputStream().write(serializer.serializeArray(goalWebs));

            InputStream is = connection.getInputStream();
            Assert.assertEquals(connection.getResponseCode(), 200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
