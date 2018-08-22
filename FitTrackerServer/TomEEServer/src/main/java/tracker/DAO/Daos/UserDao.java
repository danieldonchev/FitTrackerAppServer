package tracker.DAO.Daos;

import tracker.Entities.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, String>{
    Optional<User> findUser(String email);
    boolean changePassword(String email, String password);
    long getLastPasswordChange(String id);
}
