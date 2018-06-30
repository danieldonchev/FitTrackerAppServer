package tracker.DAO.DAOServices;

import tracker.Entities.UserPasswordToken;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.*;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserPasswordTokenService {

    @Resource
    private UserTransaction userTransaction;
    private GenericDAOImpl<UserPasswordToken, String> dao;

    @Inject
    public UserPasswordTokenService(GenericDAOImpl<UserPasswordToken, String> dao){
        this.dao = dao;
    }

    public UserPasswordToken create(UserPasswordToken passwordToken){
        try {
            userTransaction.begin();
            dao.create(passwordToken);
            userTransaction.commit();
        } catch (Exception e) {
            if(e instanceof RollbackException){
                try {
                    userTransaction.begin();
                    dao.update(passwordToken);
                    userTransaction.commit();
                } catch (Exception e1) {
                    throw new RuntimeException();
                }
            }
        }
        return passwordToken;
    }

    public UserPasswordToken find(UserPasswordToken passwordToken){
        return dao.read(UserPasswordToken.class, passwordToken.getEmail());
    }
}
