package tracker.DAO.DaoServices;

import tracker.Entities.SportActivity;

public interface SportActivityService {

    SportActivity create(SportActivity sportActivity);
    SportActivity update(SportActivity sportActivity);
    SportActivity read(String id, String userID);
    void delete(String id, String userID);
}
