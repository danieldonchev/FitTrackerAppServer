package tracker.DAO.Daos;

import tracker.Entities.User;

public interface UserDao extends GenericDao<User, String>{
    User findUser(String email);
    boolean changePassword(String email, String password);
    long getLastPasswordChange(String id);
}
