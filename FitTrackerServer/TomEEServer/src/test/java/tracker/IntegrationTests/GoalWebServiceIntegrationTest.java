package tracker.IntegrationTests;

import com.tracker.shared.Entities.GoalWeb;
import org.junit.jupiter.api.Test;
import tracker.Utils.Https.API;
import tracker.Utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;

import java.io.*;

import static tracker.Utils.Https.HttpsConnection.*;
import static tracker.Utils.TestUtils.readStream;

public class GoalWebServiceIntegrationTest {

    @Test
    public void insertGoal(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.goal);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            GoalWeb goal = new GoalWeb("dc9e60b2-5f0d-4a94-9226-c76817bfd608",
                                    1,
                                    555.42d,
                                    180l,
                                    50l,
                                    300l,
                                    1l,
                                    2l,
                                    1l);


            connection.getOutputStream().write(goal.serialize());
            InputStream is = connection.getInputStream();
           // GoalWeb goalWebReceived = new GoalWeb().deserialize(IOUtils.readFully(is, -1, true));
            String receivedStr = readStream(is);
            int b = 5;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateGoal() throws IOException{
        HttpsConnection httpsConnection = new HttpsConnection();
        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_PUT, API.goal);
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        GoalWeb goal = new GoalWeb("dc9e60b2-5f0d-4a94-9226-c76817bfd607",
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
        int b = 5;
    }

    @Test
    public void deleteGoal() throws IOException {

        HttpsConnection httpsConnection = new HttpsConnection();
        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_DELETE, API.goal + "/dc9e60b2-5f0d-4a94-9226-c76817bfd607");
        connection.setRequestProperty("Content-Type", "application/json");

        InputStream is = connection.getInputStream();
        String receivedStr = readStream(is);

        int b = 5;
    }



}
