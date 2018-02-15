package main.java.com.traker.DAO;


import com.traker.shared.Weight;
import org.json.JSONArray;

import java.util.ArrayList;

public interface WeightDAO {
    int insertWeight(Weight weight, String userID, long timestamp);
    int updateWeight(Weight weight, String userID, long timestamp);
    ArrayList<Weight> getWeights(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit);
}
