package first.workshop.judgedemo.service;

import first.workshop.judgedemo.model.entity.Exercise;
import first.workshop.judgedemo.model.service.ExerciseAddServiceModel;
import first.workshop.judgedemo.model.service.ExerciseViewServiceModel;

import java.util.List;

public interface ExerciseService {
    ExerciseAddServiceModel addExercise(ExerciseAddServiceModel exerciseAddServiceModel);

    List<ExerciseViewServiceModel> getAllExercises();

    Exercise getExerciseByName(String exerciseName);

    List<String> getExercisesById(String id);
}
