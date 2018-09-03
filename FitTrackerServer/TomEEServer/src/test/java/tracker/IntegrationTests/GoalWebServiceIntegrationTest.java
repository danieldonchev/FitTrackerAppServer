package tracker.IntegrationTests;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.serializers.GoalSerializer;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import tracker.utils.Https.API;
import tracker.utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

import static tracker.utils.Https.HttpsConnection.*;
import static tracker.utils.TestUtils.readStream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GoalWebServiceIntegrationTest {

    @Test
    public void test1_insertGoal(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.goal);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            GoalWeb goal = new GoalWeb(UUID.fromString("dc9e60b2-5f0d-4a94-9226-c76817bfd610"),
                                    1,
                                    555.42d,
                                    180L,
                                    50L,
                                    300L,
                                    1L,
                                    2L,
                                    1L);


            connection.getOutputStream().write(goal.serialize());
            InputStream is = connection.getInputStream();
            String receivedStr = readStream(is);
            JSONObject object = new JSONObject(receivedStr);

            Assert.assertNotNull(connection);
            Assert.assertEquals(connection.getResponseCode(), 200);
            Assert.assertEquals(object.getString("id"), goal.getId());
            Assert.assertEquals(object.getString("data"), GoalWeb.class.getSimpleName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2_updateGoal() throws IOException{
        HttpsConnection httpsConnection = new HttpsConnection();
        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_PUT, API.goal);
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        GoalWeb goal = new GoalWeb(UUID.fromString("dc9e60b2-5f0d-4a94-9226-c76817bfd610"),
                1,
                1235,
                23,
                567142,
                300l,
                1l,
                2l,
                1l);

        connection.getOutputStream().write(goal.serialize());
        InputStream is = connection.getInputStream();
        String receivedStr = readStream(is);
        JSONObject object = new JSONObject(receivedStr);

        Assert.assertNotNull(connection);
        Assert.assertEquals(connection.getResponseCode(), 200);
        Assert.assertEquals(object.getString("id"), goal.getId());
        Assert.assertEquals(object.getString("data"), GoalWeb.class.getSimpleName());
    }

    @Test
    public void test3_deleteGoal() throws IOException {

        String id = "dc9e60b2-5f0d-4a94-9226-c76817bfd610";
        HttpsConnection httpsConnection = new HttpsConnection();
        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_DELETE, API.goal + id);
        connection.setRequestProperty("Content-Type", "application/json");

        InputStream is = connection.getInputStream();
        String receivedStr = readStream(is);

        JSONObject object = new JSONObject(receivedStr);

        Assert.assertNotNull(connection);
        Assert.assertEquals(connection.getResponseCode(), 200);
        Assert.assertEquals(object.getString("id"), id);
        Assert.assertEquals(object.getString("data"), GoalWeb.class.getSimpleName());
    }

    @Test
    public void test4_getAllGoals() throws IOException{
        HttpsConnection httpsConnection = new HttpsConnection();
        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_GET, API.goal + "all");
        connection.setRequestProperty("Content-Type", "application/json");

        GoalSerializer serializer = new GoalSerializer();
        InputStream is = connection.getInputStream();
        ArrayList<GoalWeb> goals = serializer.deserializeArray(IOUtils.toByteArray(is));


        Assert.assertNotNull(connection);
        Assert.assertEquals(connection.getResponseCode(), 200);

    }
}
