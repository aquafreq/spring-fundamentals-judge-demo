package first.workshop.judgedemo.service;

import first.workshop.judgedemo.model.entity.User;
import first.workshop.judgedemo.model.service.CommentServiceAddModel;
import first.workshop.judgedemo.model.service.HomeworkViewServiceModel;

public interface HomeworkService {
    HomeworkViewServiceModel addHomework(String userId, String exerciseName, String githubAddress);

    boolean checkIfHomeworkDueTimeHasPassed(String exercise);

    String getAllExerciseNameByUser(User user);

    String getRandomHomeworkGithub(String currentUserId);

    void addCommentToHomework(String userId, String randomUserGitHub, CommentServiceAddModel commentServiceAddModel) throws IllegalArgumentException;
}
