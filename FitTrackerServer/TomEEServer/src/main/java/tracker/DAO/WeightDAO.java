package tracker.DAO;


import com.tracker.shared.Entities.WeightWeb;

import java.util.ArrayList;

public interface WeightDAO {
    int insertWeight(WeightWeb weightWeb, String userID, long timestamp);

    int updateWeight(WeightWeb weightWeb, String userID, long timestamp);

    ArrayList<WeightWeb> getWeights(String userID, String where, Object[] selectionArgs, String[] orderBy, int limit);
}
