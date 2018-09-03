package tracker.settings.dao;

import tracker.utils.dao.GenericDao;
import tracker.settings.Details;
import tracker.authentication.users.UserPrincipal;

import java.util.UUID;

public interface SettingsDao extends GenericDao<Details, UUID> {

    Details update(Details details, UserPrincipal user);
    Details read(UserPrincipal user);
}
