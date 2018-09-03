package tracker.IntegrationTests;

import com.tracker.shared.entities.*;
import com.tracker.shared.serializers.SportActivitySerializer;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import tracker.sportactivity.SportActivity;
import tracker.utils.Https.API;
import tracker.utils.Https.HttpsConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import static tracker.utils.Https.HttpsConnection.*;
import static tracker.utils.TestUtils.readStream;

public class SportActivityRestIntegrationTest {

    @Test
    public void insertSportActivity(){
        try {
            SportActivitySerializer serializer = new SportActivitySerializer();
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_POST, API.sportActivity);
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            SportActivityWeb sportActivityWeb = new SportActivityWeb();
            sportActivityWeb.setId(UUID.randomUUID());
            sportActivityWeb.setActivity("Running");
            sportActivityWeb.setType(2);
            sportActivityWeb.setCalories(900);
            sportActivityWeb.setTotalElevation(500);
            sportActivityWeb.setTotalDenivelation(400);
            sportActivityWeb.setStartTimestamp(12326124);
            sportActivityWeb.setEndTimestamp(1437328742);
            sportActivityWeb.setLastModified(34123);

            ArrayList<Point> points = new ArrayList<>();
            points.add(new Point(500, 600, 300, 10));
            points.add(new Point(500, 600, 300, 10));
            points.add(new Point(500, 600, 300, 10));
            points.add(new Point(500, 600, 300, 10));
            points.add(new Point(500, 600, 300, 10));

            ArrayList<Split> splits = new ArrayList<>();
            splits.add(new Split(5, 20));
            splits.add(new Split(5, 20));
            splits.add(new Split(5, 20));
            splits.add(new Split(5, 20));

            ArrayList<Data> datas = new ArrayList<>();
            datas.add(new Data(300, 300, 50, 160, 7.1f, 6.3f));
            datas.add(new Data(300, 300, 50, 160, 7.1f, 6.3f));
            datas.add(new Data(300, 300, 50, 160, 7.1f, 6.3f));
            datas.add(new Data(300, 300, 50, 160, 7.1f, 6.3f));

            sportActivityWeb.setPoints(points);
            sportActivityWeb.setDatas(datas);
            sportActivityWeb.setSplits(splits);

            connection.getOutputStream().write(serializer.serialize(sportActivityWeb));
            InputStream is = connection.getInputStream();
            String receivedStr = readStream(is);
            JSONObject object = new JSONObject(receivedStr);
            Assert.assertNotNull(connection);
            Assert.assertEquals(connection.getResponseCode(), 200);
            Assert.assertEquals(object.getString("id"), sportActivityWeb.getId());
            Assert.assertEquals(object.getString("data"), SportActivityWeb.class.getSimpleName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSportActivity(){
        try {
            HttpsConnection httpsConnection = new HttpsConnection();
            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_GET, API.sportActivity + "/9d866181-26e9-4309-99b7-c0dac00d5349" + "/29cb45ef-570c-41cc-abfa-421bce0cac61");
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            InputStream is = connection.getInputStream();
            SportActivitySerializer serializer = new SportActivitySerializer();
            SportActivityWeb sportActivity = serializer.deserialize(IOUtils.toByteArray(is));


            Assert.assertEquals(connection.getResponseCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void updateSportActivity(){
//        try {
//            SportActivitySerializer serializer = new SportActivitySerializer();
//            HttpsConnection httpsConnection = new HttpsConnection();
//            HttpsURLConnection connection = httpsConnection.getConnection(HTTP_PUT, API.sportActivity);
//            connection.setRequestProperty("Content-Type", "application/octet-stream");
//            ArrayList<SplitWeb> splitWebs = new ArrayList<>();
//            splitWebs.add(new SplitWeb(1, 5, 5));
//            splitWebs.add(new SplitWeb(2, 7, 7));
//            SportActivityWeb sportActivityWeb = new SportActivityWeb("469b4f22-09d3-40a5-ab65-33b81c45e573",
//                    "Running",
//                    5,
//                    6,
//                    7000,
//                    800,
//                    200,
//                    300,
//                    70);
//            sportActivityWeb.setSplitWebs(splitWebs);
//            SportActivityMap sportActivityMap = new SportActivityMap();
//            ArrayList<LatLng> markers = new ArrayList<>();
//            markers.add(new LatLng(5, 6));
//            markers.add(new LatLng(7, 8));
//            sportActivityMap.setMarkers(markers);
//            sportActivityWeb.setSportActivityMap(sportActivityMap);
//
//
//            connection.getOutputStream().write(serializer.serialize(sportActivityWeb));
//            InputStream is = connection.getInputStream();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void deleteSportActivity() throws IOException {
//        String id = "dc9e60b2-5f0d-4a94-9226-c76817bfd607";
//        HttpsConnection httpsConnection = new HttpsConnection();
//        HttpsURLConnection connection = httpsConnection.getConnection(HTTP_DELETE, API.sportActivity + id);
//        connection.setRequestProperty("Content-Type", "application/json");
//
//        InputStream is = connection.getInputStream();
//        String receivedStr = readStream(is);
//
//        JSONObject object = new JSONObject(receivedStr);
//
//        Assert.assertNotNull(connection);
//        Assert.assertEquals(connection.getResponseCode(), 200);
//        Assert.assertEquals(object.getString("id"), id);
//        Assert.assertEquals(object.getString("data"), SportActivityWeb.class.getSimpleName());
//    }

}
