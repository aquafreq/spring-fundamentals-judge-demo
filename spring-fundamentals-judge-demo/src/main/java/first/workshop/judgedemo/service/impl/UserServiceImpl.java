package first.workshop.judgedemo.service.impl;

import first.workshop.judgedemo.model.entity.Comment;
import first.workshop.judgedemo.model.entity.Role;
import first.workshop.judgedemo.model.entity.User;
import first.workshop.judgedemo.model.service.UserServiceLoginModel;
import first.workshop.judgedemo.model.service.UserServiceModel;
import first.workshop.judgedemo.model.service.UserServiceProfileInfoModel;
import first.workshop.judgedemo.model.service.UserServiceRegisterModel;
import first.workshop.judgedemo.repository.UserRepository;
import first.workshop.judgedemo.service.HomeworkService;
import first.workshop.judgedemo.service.RoleService;
import first.workshop.judgedemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final HomeworkService homeworkService;
    private final ModelMapper modelMapper;

    //lazy init is bad practice,
    // but 2 lazy to fix the circular dependency problem
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleService roleService,
                           @Lazy HomeworkService homeworkService,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.homeworkService = homeworkService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserServiceRegisterModel getUserServiceModelByNameAndPassword(String username, String password) {
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
                                    roleService.getRole("ADMIN").get() : roleService.getRole("USER").get());

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

    @Override
    public UserServiceModel getUserServiceModelById(String id) {
        return modelMapper.map(userRepository.getOne(id), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel changeUserRole(String username, String changeRole) throws IllegalArgumentException {
        Optional<Role> optionalRole = roleService.getRole(changeRole);
        Role dbRole = optionalRole.orElseThrow(() -> new IllegalArgumentException("no such role"));

        String dbRoleName = dbRole.getName().toString();
        Optional<User> optionalUser = userRepository.getByUsername(username);

        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("no such user"));

        if (user.getRole().getName().toString().equals(dbRoleName)) {
            throw new IllegalArgumentException("user has such this role already");
        }

        user.setRole(dbRole);
        userRepository.saveAndFlush(user);

        return modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.getById(id);
    }

    @Override
    public User getUserByGithubAddress(String githubAddress) {
        return userRepository.getByGit(githubAddress);
    }

    @Override
    public String getUserExercisesById(String id) {
        User user = userRepository
                .getById(id);

        return homeworkService.getAllExerciseNameByUser(user);
    }

    @Override
    public UserServiceProfileInfoModel getUserProfileInfo(String id) {
        User userById = getUserById(id);

        UserServiceProfileInfoModel profileInfoModel =
                modelMapper.map(userById, UserServiceProfileInfoModel.class);
        profileInfoModel
                .setSentHomework(homeworkService.getAllExerciseNameByUser(userById));
        return profileInfoModel;
    }

    @Override
    public List<String> getAllUsernameByAverageGrade() {
        return userRepository
                .findAll()
                .stream()
                .sorted(this::compareUsersAverageScore)
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    private int compareUsersAverageScore(User u1, User u2) {
        double secondUserAverageGrade = averageScore(u2);
        double firstUserAverageGrade = averageScore(u1);

        return (int)(secondUserAverageGrade - firstUserAverageGrade);
    }

    private double averageScore(User u2) {
        return u2.getComments()
                .stream()
                .mapToDouble(Comment::getScore)
                .average()
                .orElse(0D);
    }
}