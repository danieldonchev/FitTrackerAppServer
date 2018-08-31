package tracker.weight;

import tracker.utils.dao.GenericDao;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class WeightServiceImpl implements WeightService{

    private GenericDao<Weight, WeightKey> dao;

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
