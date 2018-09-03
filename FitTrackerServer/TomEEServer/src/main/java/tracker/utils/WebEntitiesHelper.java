package tracker.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tracker.shared.entities.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import org.json.JSONArray;
import org.json.JSONObject;
import tracker.authentication.users.UserPrincipal;
import tracker.goal.Goal;
import tracker.sportactivity.SportActivity;
import tracker.weight.Weight;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class WebEntitiesHelper {

    public WebEntitiesHelper(){

    }

    public SportActivity toSportActivity(SportActivityWeb sportActivityWeb, UUID userID, long timestamp){

        SportActivity sportActivity = new SportActivity(
                sportActivityWeb.getId(),
                userID,
                sportActivityWeb.getActivity(),
                sportActivityWeb.getDistance(),
                sportActivityWeb.getSteps(),
                sportActivityWeb.getStartTimestamp(),
                sportActivityWeb.getEndTimestamp(),
                sportActivityWeb.getLastModified(),
                timestamp);

        JsonObject object = new JsonObject();
        Gson gson = new Gson();

        object.add("datas" ,gson.toJsonTree(sportActivityWeb.getDatas()));
        object.add("points", gson.toJsonTree(sportActivityWeb.getPoints()));
        object.add("splits", gson.toJsonTree(sportActivityWeb.getSplits()));

        sportActivity.setData(object.toString());

        return sportActivity;
    }

    public SportActivityWeb toSportActivityWeb(SportActivity sportActivity){
        SportActivityWeb sportActivityWeb = new SportActivityWeb();

        sportActivityWeb.setId(sportActivity.getId());
        sportActivityWeb.setCalories(sportActivity.getCalories());
        sportActivityWeb.setStartTimestamp(sportActivity.getStartTimestamp());
        sportActivityWeb.setEndTimestamp(sportActivity.getEndTimestamp());
        sportActivityWeb.setLastModified(sportActivity.getLastModified());
        sportActivityWeb.setActivity(sportActivity.getActivity());


        JsonObject object = new JsonParser().parse(sportActivity.getData()).getAsJsonObject();


        ArrayList<Data> datas = new Gson().fromJson(object.get("datas"), new TypeToken<ArrayList<Data>>() {}.getType());
        ArrayList<Point> points = new Gson().fromJson(object.get("points"), new TypeToken<ArrayList<Point>>() {}.getType());
        ArrayList<Split> splits = new Gson().fromJson(object.get("splits"), new TypeToken<ArrayList<Split>>() {}.getType());

        sportActivityWeb.setDatas(datas);
        sportActivityWeb.setPoints(points);
        sportActivityWeb.setSplits(splits);

        return sportActivityWeb;
    }

    public GoalWeb toGoalWeb(Goal goal){
        GoalWeb goalWeb = new GoalWeb(
                goal.getId(),
                goal.getType(),
                goal.getDistance(),
                goal.getDuration(),
                goal.getCalories(),
                goal.getSteps(),
                goal.getFromDate(),
                goal.getToDate(),
                goal.getLastModified()
        );
        return goalWeb;
    }

    public Goal toGoal(GoalWeb goalWeb, UserPrincipal user){
        Goal goal = new Goal(
                goalWeb.getId(),
                user.getId(),
                goalWeb.getType(),
                goalWeb.getDistance(),
                goalWeb.getDuration(),
                goalWeb.getCalories(),
                goalWeb.getSteps(),
                goalWeb.getFromDate(),
                goalWeb.getToDate(),
                goalWeb.getLastModified(),
                user.getNewServerTimestamp()
        );
        return goal;
    }

    public Weight toWeight(WeightWeb weightWeb, UserPrincipal user){
        Weight weight = new Weight(user.getId(),
                weightWeb.getDate(),
                weightWeb.getWeight(),
                weightWeb.getLastModified(),
                user.getNewServerTimestamp());
        return weight;
    }

    public WeightWeb toWeightWeb(Weight weight){
        WeightWeb weightWeb = new WeightWeb(
                weight.getWeight(),
                weight.getWeightKey().getDate(),
                weight.getLastModified());
        return weightWeb;
    }

}
