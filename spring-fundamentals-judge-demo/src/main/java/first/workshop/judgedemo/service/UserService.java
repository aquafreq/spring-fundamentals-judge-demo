package first.workshop.judgedemo.service;

import first.workshop.judgedemo.model.entity.User;
import first.workshop.judgedemo.model.service.UserServiceLoginModel;
import first.workshop.judgedemo.model.service.UserServiceModel;
import first.workshop.judgedemo.model.service.UserServiceProfileInfoModel;
import first.workshop.judgedemo.model.service.UserServiceRegisterModel;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserServiceRegisterModel getUserServiceModelByNameAndPassword(String username, String password);

    void saveUser(UserServiceRegisterModel userServiceRegisterModel);

    Optional<UserServiceLoginModel> logUser(UserServiceLoginModel loginModel);

    UserServiceModel getUserServiceModelById(String id);

    List<UserServiceModel> getAllUsers();

    UserServiceModel changeUserRole(String username, String role);

    User getUserById(String id);

    User getUserByGithubAddress(String githubAddress);

    String getUserExercisesById(String id);

    UserServiceProfileInfoModel getUserProfileInfo(String id);

    List<String> getAllUsernameByAverageGrade();

}
