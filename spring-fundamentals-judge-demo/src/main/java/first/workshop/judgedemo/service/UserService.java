package first.workshop.judgedemo.service;

import first.workshop.judgedemo.model.service.UserServiceLoginModel;
import first.workshop.judgedemo.model.service.UserServiceRegisterModel;

import java.util.Optional;

public interface UserService {
    UserServiceRegisterModel getUserServiceModelByName(String username, String password);

    void saveUser(UserServiceRegisterModel userServiceRegisterModel);

    Optional<UserServiceLoginModel> logUser(UserServiceLoginModel loginModel);
}
