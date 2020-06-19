package first.workshop.judgedemo.service.impl;

import first.workshop.judgedemo.model.entity.Exercise;
import first.workshop.judgedemo.model.service.ExerciseAddServiceModel;
import first.workshop.judgedemo.model.service.ExerciseViewServiceModel;
import first.workshop.judgedemo.repository.ExerciseRepository;
import first.workshop.judgedemo.service.ExerciseService;
import first.workshop.judgedemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ModelMapper modelMapper;
    private final ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ModelMapper modelMapper, ExerciseRepository exerciseRepository) {
        this.modelMapper = modelMapper;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public ExerciseAddServiceModel addExercise(ExerciseAddServiceModel exerciseAddServiceModel) {
        Exercise save = exerciseRepository.save(modelMapper.map(exerciseAddServiceModel, Exercise.class));
        return modelMapper.map(save, ExerciseAddServiceModel.class);
    }

    @Override
    public List<ExerciseViewServiceModel> getAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, ExerciseViewServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Exercise getExerciseByName(String exerciseName) {
        return exerciseRepository.getByName(exerciseName);
    }

    @Override
    public List<String> getExercisesById(String id) {
        return
                exerciseRepository.getById(id);
    }
}
