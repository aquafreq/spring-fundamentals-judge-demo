package first.workshop.judgedemo.repository;

import first.workshop.judgedemo.model.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, String> {
    Exercise getByName(String exerciseName);

    List<String> getById(String id);
}
