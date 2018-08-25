package tracker.DAO.Daos;

import tracker.Entities.Details;
import tracker.Entities.GenericUser;

import java.util.UUID;

public interface SettingsDao extends GenericDao<Details, UUID>{

    Details update(Details details, GenericUser user);
    Details read(GenericUser user);
}
