package tracker.authenticate;

public interface UserPasswordTokenService {
    UserPasswordToken create(UserPasswordToken token);
    UserPasswordToken find(UserPasswordToken token);
}
