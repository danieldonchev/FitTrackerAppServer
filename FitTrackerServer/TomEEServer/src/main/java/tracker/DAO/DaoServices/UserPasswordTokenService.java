package tracker.DAO.DaoServices;

import tracker.Entities.UserPasswordToken;

public interface UserPasswordTokenService {
    UserPasswordToken create(UserPasswordToken token);
    UserPasswordToken find(UserPasswordToken token);
}
