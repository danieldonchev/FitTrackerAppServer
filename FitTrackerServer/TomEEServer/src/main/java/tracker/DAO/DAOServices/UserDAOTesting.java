package tracker.DAO.DAOServices;


import tracker.Entities.User;

import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

@Alternative
public class UserDAOTesting extends GenericDAOImpl<User, String> {

    /*
        Finds user by email.
        @param email User email.
        @return User
     */
    public User findUser(String email){
        Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE email=:arg1");
        query.setParameter("arg1", email);
        User userFromDB = (User) query.getSingleResult();
        return userFromDB;
    }

    public boolean changePassword(String email, String password) {
        try {
            Query query = getEntityManager().createQuery("UPDATE User SET Passhash = :arg1 WHERE email=:arg2");
            query.setParameter("arg1", password);
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
        Query query = getEntityManager().createQuery("select password_last_modified from users where id=:arg1");
        query.setParameter("arg1", id);
        return  (Long) query.getSingleResult();
    }
}
