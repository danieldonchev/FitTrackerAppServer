package tracker.DAO.Daos;

import tracker.Entities.Details;
import tracker.Entities.GenericUser;

public interface SettingsDao extends GenericDao<Details, String>{

    Details update(Details details, GenericUser user);
    Details read(GenericUser user);
}
