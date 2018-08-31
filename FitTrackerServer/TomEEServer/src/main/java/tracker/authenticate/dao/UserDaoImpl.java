package tracker.authenticate.dao;


import tracker.authenticate.utils.PasswordGenerator;
import tracker.utils.dao.GenericDAOImpl;
import tracker.authenticate.User;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.Optional;
import java.util.UUID;

@UserDaoQualifier
public class UserDaoImpl extends GenericDAOImpl<User, UUID> implements UserDao {

    /*
        Finds user by email.
        @param email User email.
        @return User If user is not found returns null.
     */
    @Override
    public Optional<User> findUser(String email){
        Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE email=:arg1");
        query.setParameter("arg1", email);
        Optional<User> user;
        try{
            user = Optional.of((User) query.getSingleResult());
        } catch (NoResultException e){
            user = Optional.empty();
        }

        return user;
    }

    /*
        Updates the user password in the database.
     */
    @Override
    public boolean changePassword(String email, String password) {
        try {
            PasswordGenerator generator = new PasswordGenerator();
            Query query = getEntityManager().createQuery("UPDATE User SET password = :arg1 WHERE email=:arg2");
            query.setParameter("arg1", generator.generatePasswordHash(password));
            query.setParameter("arg2", email);
            query.executeUpdate();
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    /*
        Gets the last time the password was change in UTC.
        @param id User id.
        @return Long
     */
    public long getLastPasswordChange(UUID id){
        Query query = getEntityManager().createQuery("select lastPassChange from User where id=:arg1");
        query.setParameter("arg1", id);
        return  (Long) query.getSingleResult();
    }
}
