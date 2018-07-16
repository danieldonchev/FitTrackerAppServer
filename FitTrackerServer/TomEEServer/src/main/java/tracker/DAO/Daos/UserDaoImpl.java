package tracker.DAO.Daos;


import tracker.Authenticate.PasswordGenerator;
import tracker.Entities.User;

import javax.enterprise.inject.Alternative;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

@Alternative
public class UserDaoImpl extends GenericDAOImpl<User, String> implements UserDao {

    /*
        Finds user by email.
        @param email User email.
        @return User
     */
    @Override
    public User findUser(String email){
        Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE email=:arg1");
        query.setParameter("arg1", email);
        User userFromDB = (User) query.getSingleResult();
        return userFromDB;
    }

    /*
        Updates the user password in the database.
     */
    @Override
    public boolean changePassword(String email, String password) {
        try {
            PasswordGenerator generator = new PasswordGenerator();
            Query query = getEntityManager().createQuery("UPDATE User SET Passhash = :arg1 WHERE email=:arg2");
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
    public long getLastPasswordChange(String id){
        Query query = getEntityManager().createQuery("select lastPassChange from User where id=:arg1");
        query.setParameter("arg1", id);
        return  (Long) query.getSingleResult();
    }
}
