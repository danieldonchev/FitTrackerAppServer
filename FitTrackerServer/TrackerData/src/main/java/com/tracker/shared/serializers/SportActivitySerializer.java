package com.tracker.shared.serializers;

import com.google.flatbuffers.FlatBufferBuilder;
import com.tracker.shared.entities.Data;
import com.tracker.shared.entities.Point;
import com.tracker.shared.entities.Split;
import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.flatbuf.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

public class SportActivitySerializer implements FlatbufferSerializer<SportActivityWeb> {


    @Override
    public SportActivityWeb deserialize(byte[] bytes){

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        SportActivityFlat bufferer =  SportActivityFlat.getRootAsSportActivityFlat(buf);

        return deserializeSportActivity(bufferer);
    }

    @Override
    public ArrayList<SportActivityWeb> deserializeArray(byte[] bytes){
        ArrayList<SportActivityWeb> sportActivityWebs = new ArrayList<>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        SportActivitiesFlat bufferer = SportActivitiesFlat.getRootAsSportActivitiesFlat(buf);
        for(int i = 0; i < bufferer.sportActivitiesLength(); i++){
            SportActivityFlat sportActivityFlat = bufferer.sportActivities(i);
            sportActivityWebs.add(deserializeSportActivity(sportActivityFlat));
        }
        return sportActivityWebs;
    }

    @Override
    public byte[] serialize(SportActivityWeb sportActivityWeb){
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int finish = getSportActivityInt(builder, sportActivityWeb);

        builder.finish(finish);

        return builder.sizedByteArray();
    }

    @Override
    public byte[] serializeArray(ArrayList<SportActivityWeb> list){
        FlatBufferBuilder builder = new FlatBufferBuilder(0);

        int [] offset = new int[list.size()];
        for(int i = list.size() - 1; i >= 0; i--){
            offset[i] = getSportActivityInt(builder, list.get(i));
        }
        int vector = SportActivitiesFlat.createSportActivitiesVector(builder, offset);

        SportActivitiesFlat.startSportActivitiesFlat(builder);
        SportActivitiesFlat.addSportActivities(builder, vector);
        int finish = SportActivitiesFlat.endSportActivitiesFlat(builder);

        builder.finish(finish);

        return builder.sizedByteArray();
    }

    public int getSportActivityInt(FlatBufferBuilder builder, SportActivityWeb sportActivityWeb){
        int idString = builder.createString(sportActivityWeb.getId().toString());
        int activityString = builder.createString(sportActivityWeb.getActivity());
        int splitsInt = getSplitsInt(builder, sportActivityWeb.getSplits());
        int datasInt = getDatasInt(builder, sportActivityWeb.getDatas());
        int pointsInt = getPointsInt(builder, sportActivityWeb.getPoints());

        SportActivityFlat.startSportActivityFlat(builder);
        SportActivityFlat.addId(builder, idString);
        SportActivityFlat.addActivity(builder, activityString);
        SportActivityFlat.addStartTimestamp(builder, sportActivityWeb.getStartTimestamp());
        SportActivityFlat.addEndTimestamp(builder, sportActivityWeb.getEndTimestamp());
        SportActivityFlat.addType(builder, sportActivityWeb.getType());
        SportActivityFlat.addTotalElevation(builder, sportActivityWeb.getTotalElevation());
        SportActivityFlat.addTotalDenivelation(builder, sportActivityWeb.getTotalDenivelation());
        SportActivityFlat.addLastModified(builder, sportActivityWeb.getLastModified());
        SportActivityFlat.addDataList(builder, datasInt);
        SportActivityFlat.addPoints(builder, pointsInt);
        SportActivityFlat.addSplits(builder, splitsInt);

        return SportActivityFlat.endSportActivityFlat(builder);
    }

    public int getSplitsInt(FlatBufferBuilder builder, ArrayList<Split> splits){
//        SportActivityFlat.create()
//        SportActivityFlat.startSplitsVector(builder, splits.size());
        int[] offsets = new int[splits.size()];
        for(int i = splits.size() - 1; i >= 0; i--){
            offsets[i] = SplitFlat.createSplitFlat(builder,
                                                    splits.get(i).getStartIndex(),
                                                    splits.get(i).getEndIndex());
        }
        return SportActivityFlat.createSplitsVector(builder, offsets);
    }

    public int getDatasInt(FlatBufferBuilder builder, ArrayList<Data> datas){
        int[] offsets = new int[datas.size()];
        //SportActivityFlat.startDataListVector(builder, datas.size());
        for(int i = datas.size() - 1; i >= 0; i--){
            offsets[i] = DataFlat.createDataFlat(builder,
                    datas.get(i).getDistance(),
                    datas.get(i).getDuration(),
                    datas.get(i).getSteps(),
                    datas.get(i).getSteps(),
                    datas.get(i).getAvgSpeed(),
                    datas.get(i).getCurrentSpeed());
        }

        return SportActivityFlat.createDataListVector(builder, offsets);
    }

    public int getPointsInt(FlatBufferBuilder builder, ArrayList<Point> points){
        //int[] offsets = new int[points.size()];
        SportActivityFlat.startPointsVector(builder, points.size());
        for(int i = points.size() - 1; i >= 0; i--){
             PointFlat.createPointFlat(builder,
                    points.get(i).getLat(),
                    points.get(i).getLon(),
                    points.get(i).getElevation(),
                    points.get(i).getGpsAccuracy());

        }
        int vector = builder.endVector();

        return vector;
    }

    public SportActivityWeb deserializeSportActivity(SportActivityFlat bufferer){
        SportActivityWeb sportActivityWeb = new SportActivityWeb();
        sportActivityWeb.setId(UUID.fromString(bufferer.id()));
        sportActivityWeb.setActivity(bufferer.activity());
        sportActivityWeb.setType(bufferer.type());
        sportActivityWeb.setCalories(bufferer.calories());
        sportActivityWeb.setTotalElevation(bufferer.totalElevation());
        sportActivityWeb.setTotalDenivelation(bufferer.totalDenivelation());
        sportActivityWeb.setStartTimestamp(bufferer.startTimestamp());
        sportActivityWeb.setEndTimestamp(bufferer.endTimestamp());
        sportActivityWeb.setLastModified(bufferer.lastModified());

        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < bufferer.pointsLength(); i++){
            PointFlat pointFlat = bufferer.points(i);
            points.add(new Point(pointFlat.lat(),
                    pointFlat.lon(),
                    pointFlat.elevation(),
                    pointFlat.gpsAccuracy()));
        }

        ArrayList<Data> datas = new ArrayList<Data>();
        for(int i = 0; i < bufferer.dataListLength(); i++){
            DataFlat dataFlat = bufferer.dataList(i);
            datas.add(new Data(dataFlat.distance(),
                    dataFlat.duration(),
                    dataFlat.steps(),
                    dataFlat.cadence(),
                    dataFlat.avgSpeed(),
                    dataFlat.currentSpeed()));
        }

        ArrayList<Split> splits = new ArrayList<>();
        for(int i = 0; i < bufferer.splitsLength(); i++){
            SplitFlat splitFlat = bufferer.splits(i);
            splits.add(new Split(splitFlat.startIndex(), splitFlat.endIndex()));
        }

        sportActivityWeb.setPoints(points);
        sportActivityWeb.setDatas(datas);
        sportActivityWeb.setSplits(splits);

        return sportActivityWeb;
    }
}
