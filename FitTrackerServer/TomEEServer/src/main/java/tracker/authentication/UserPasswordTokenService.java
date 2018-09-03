package tracker.authentication;

public interface UserPasswordTokenService {
    UserPasswordToken create(UserPasswordToken token);
    UserPasswordToken find(UserPasswordToken token);
}
