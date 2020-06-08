package first.workshop.judgedemo.service.impl;

import first.workshop.judgedemo.model.entity.User;
import first.workshop.judgedemo.model.service.UserServiceLoginModel;
import first.workshop.judgedemo.model.service.UserServiceRegisterModel;
import first.workshop.judgedemo.repository.UserRepository;
import first.workshop.judgedemo.service.RoleService;
import first.workshop.judgedemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserServiceRegisterModel getUserServiceModelByName(String username, String password) {
        User user = userRepository
                .getByUsernameAndPassword(username, password).orElse(null);

        return user == null ? null : modelMapper.map(user, UserServiceRegisterModel.class);
    }

    //fixme
    @Override
    public void saveUser(UserServiceRegisterModel userServiceRegisterModel) {
        userRepository
                .getByUsernameAndPassword(userServiceRegisterModel.getUsername(), userServiceRegisterModel.getPassword())
                .ifPresentOrElse(
                        (r) -> System.out.println(r.getUsername() + " gg existing user, to do sth more smisleno"),
                        () -> {
                            User user = modelMapper.map(userServiceRegisterModel, User.class);

                            user.setRole(userRepository.count() == 0 ?
                                            roleService.getRole("ADMIN") : roleService.getRole("user"));

                            userRepository.save(user);
                        });
    }

    @Override
    public Optional<UserServiceLoginModel> logUser(UserServiceLoginModel loginModel) {
        Optional<User> optionalUser = userRepository.getByUsernameAndPassword(loginModel.getUsername(), loginModel.getPassword());

        if (optionalUser.isPresent()) {
            return Optional.of(modelMapper.map(optionalUser.get(), UserServiceLoginModel.class));
        }

        throw new EntityNotFoundException("no such user to have registered");
    }
}
