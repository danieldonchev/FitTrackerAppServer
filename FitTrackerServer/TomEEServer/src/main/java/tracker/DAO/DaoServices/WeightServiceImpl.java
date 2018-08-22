package tracker.DAO.DaoServices;

import tracker.DAO.Daos.GenericDao;
import tracker.Entities.Weight;
import tracker.Entities.WeightKey;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WeightServiceImpl implements WeightService{

    GenericDao<Weight, WeightKey> dao;

    public WeightServiceImpl() {
    }

    @Inject
    public WeightServiceImpl(GenericDao<Weight, WeightKey> dao) {
        this.dao = dao;
    }

    public Weight create(Weight weight){
        return dao.create(weight);
    }
}