package tracker.sportactivity;

import tracker.sportactivity.SportActivity;

import java.util.UUID;

public interface SportActivityService {

    SportActivity create(SportActivity sportActivity);
    SportActivity update(SportActivity sportActivity);
    SportActivity read(UUID id, UUID userID);
    void delete(UUID id, UUID userID);
}
