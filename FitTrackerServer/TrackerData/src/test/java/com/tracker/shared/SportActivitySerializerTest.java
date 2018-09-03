package com.tracker.shared;

import com.tracker.shared.entities.Data;
import com.tracker.shared.entities.Point;
import com.tracker.shared.entities.Split;
import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.serializers.SportActivitySerializer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class SportActivitySerializerTest {

    @Test
    public void serializationTest(){
        ArrayList<SportActivityWeb> list = new ArrayList<>();
        SportActivitySerializer serializer = new SportActivitySerializer();
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

        list.add(sportActivityWeb);
        list.add(sportActivityWeb);
        list.add(sportActivityWeb);
        list.add(sportActivityWeb);

        byte[] bytes = serializer.serializeArray(list);

        ArrayList<SportActivityWeb> test = serializer.deserializeArray(bytes);

        Assert.assertEquals(sportActivityWeb.getId(), test.get(0).getId());
    }
}
