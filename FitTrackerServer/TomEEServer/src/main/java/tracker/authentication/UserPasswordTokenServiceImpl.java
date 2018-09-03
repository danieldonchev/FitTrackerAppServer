package tracker.authentication;

import tracker.utils.dao.GenericDao;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import javax.transaction.UserTransaction;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserPasswordTokenServiceImpl implements UserPasswordTokenService{

    @Resource
    private UserTransaction userTransaction;
    private GenericDao<UserPasswordToken, String> dao;

    @Inject
    public UserPasswordTokenServiceImpl(GenericDao<UserPasswordToken, String> dao){
        this.dao = dao;
    }

    /*
        Creates a entity from UserPasswordToken into the database
        @param passwordToken
        @return     The inserted entity.
     */
    @Override
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

    /*
        Finds user by email.
        @param passwordToken
        @return     UserPasswordToken entity if record is found, else null.
     */
    @Override
    public UserPasswordToken find(UserPasswordToken passwordToken){
        return dao.read(UserPasswordToken.class, passwordToken.getEmail());
    }
}
