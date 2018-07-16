package tracker.DAO.DAOServices;

import tracker.DAO.Daos.GenericDao;
import tracker.DAO.Daos.SettingsDao;
import tracker.Entities.Details;
import tracker.Entities.Users.GenericUser;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserSettignsService {

    private SettingsDao dao;

    @Inject
    public UserSettignsService(SettingsDao dao) {
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
