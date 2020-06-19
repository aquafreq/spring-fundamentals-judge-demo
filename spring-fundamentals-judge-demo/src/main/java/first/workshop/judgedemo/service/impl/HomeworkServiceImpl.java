package first.workshop.judgedemo.service.impl;

import first.workshop.judgedemo.model.entity.Comment;
import first.workshop.judgedemo.model.entity.Exercise;
import first.workshop.judgedemo.model.entity.Homework;
import first.workshop.judgedemo.model.entity.User;
import first.workshop.judgedemo.model.service.CommentServiceAddModel;
import first.workshop.judgedemo.model.service.HomeworkViewServiceModel;
import first.workshop.judgedemo.repository.HomeworkRepository;
import first.workshop.judgedemo.service.ExerciseService;
import first.workshop.judgedemo.service.HomeworkService;
import first.workshop.judgedemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class HomeworkServiceImpl implements HomeworkService {
    private final UserService userService;
    private final ExerciseService exerciseService;
    private final HomeworkRepository repository;
    private final ModelMapper mapper;

    public HomeworkServiceImpl(UserService userService, ExerciseService exerciseService, ModelMapper mapper,
                               HomeworkRepository repository) {
        this.userService = userService;
        this.exerciseService = exerciseService;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public HomeworkViewServiceModel addHomework(String userId, String exerciseName, String githubAddress) {
        Exercise exercise = exerciseService.getExerciseByName(exerciseName);
        User user = userService.getUserById(userId);

        Homework homework = new Homework(githubAddress, user, exercise);
        return mapper.map(repository.saveAndFlush(homework), HomeworkViewServiceModel.class);
    }

    @Override
    public boolean checkIfHomeworkDueTimeHasPassed(String exercise) {
        Exercise ex = exerciseService.getExerciseByName(exercise);
        return LocalDateTime.now().isAfter(ex.getDueDate());
    }

    @Override
    public String getAllExerciseNameByUser(User user) {
        return repository.getByAuthor(user)
                .stream()
                .map(e -> e.getExercise().getName())
                .collect(Collectors.joining(", "));
    }

    @Override
    public String getRandomHomeworkGithub(String currentUserId) {
        if (repository.count() == 0) {
            throw new IllegalArgumentException("no homework added");
        }

        List<Homework> fromOtherUsers = repository.getHomeworkFromOtherUsers(currentUserId);
        Homework randomHomework = chooseRandomHomework(fromOtherUsers);
        return randomHomework.getGitAddress();
    }

    @Override
    public void addCommentToHomework(String userId, String randomUserGitHub, CommentServiceAddModel commentServiceAddModel) throws IllegalArgumentException {
        Homework homework;

        try {
            homework = repository.getByGitAddress(randomUserGitHub).get(0);
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("no homework to be checked yet");
        }

        User user = userService.getUserById(userId);

        Comment comment = mapper.map(commentServiceAddModel, Comment.class);
        comment.setAuthor(user);
        comment.setHomework(homework);
        homework
                .getComments()
                .add(comment);

        repository.saveAndFlush(homework);
    }

    private Homework chooseRandomHomework(List<Homework> fromOtherUsers) {
        int random = new Random().nextInt(2);
        return fromOtherUsers
                .stream()
                .limit(3)
                .collect(Collectors.toList())
                .get(Math.min(0, random));
    }
}