package tracker.authentication.dao;

import tracker.utils.dao.GenericDao;
import tracker.authentication.users.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDao extends GenericDao<User, UUID> {
    Optional<User> findUser(String email);
    boolean changePassword(String email, String password);
    long getLastPasswordChange(UUID id);
}
