package tracker.settings.dao;

import tracker.utils.dao.GenericDao;
import tracker.settings.Details;
import tracker.authenticate.GenericUser;

import java.util.UUID;

public interface SettingsDao extends GenericDao<Details, UUID> {

    Details update(Details details, GenericUser user);
    Details read(GenericUser user);
}
