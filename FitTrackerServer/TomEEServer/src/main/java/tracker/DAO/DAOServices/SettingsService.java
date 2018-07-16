package tracker.DAO.DAOServices;

import tracker.DAO.Daos.SettingsDao;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class SettingsService {

    SettingsDao dao;

    public SettingsService(){}

    @Inject
    public SettingsService(SettingsDao dao){
        this.dao = dao;
    }
}
