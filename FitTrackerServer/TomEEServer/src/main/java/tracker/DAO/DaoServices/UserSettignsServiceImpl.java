package tracker.DAO.DaoServices;

import tracker.DAO.Daos.SettingsDao;
import tracker.Entities.Details;
import tracker.Entities.GenericUser;
import tracker.Qualifiers.SettingsDaoQualifier;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserSettignsServiceImpl implements UserSettignsService {

    private SettingsDao dao;

    @Inject
    public UserSettignsServiceImpl(@SettingsDaoQualifier SettingsDao dao) {
        this.dao = dao;
    }

    public Details create(Details holder){
        return dao.create(holder);
    }

    public Details update(Details holder, GenericUser user){
        return dao.update(holder, user);
    }

    public Details get(GenericUser user){
        return dao.read(user);
    }
}
