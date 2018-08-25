package tracker.DAO.Daos;

import tracker.Entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao extends GenericDao<User, UUID>{
    Optional<User> findUser(String email);
    boolean changePassword(String email, String password);
    long getLastPasswordChange(UUID id);
}
