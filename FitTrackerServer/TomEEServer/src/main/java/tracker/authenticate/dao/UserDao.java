package tracker.authenticate.dao;

import tracker.utils.dao.GenericDao;
import tracker.authenticate.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao extends GenericDao<User, UUID> {
    Optional<User> findUser(String email);
    boolean changePassword(String email, String password);
    long getLastPasswordChange(UUID id);
}
