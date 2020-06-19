package first.workshop.judgedemo.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ExerciseAddServiceModel {
    @NotBlank
    @NonNull
    @Length(min = 3, message = "Exercise name length must be more than 2 characters")
    private String name;

    @NotBlank
    @NonNull
    @PastOrPresent(message = "The date cannot be in the future!")
    private LocalDateTime startedOn;

    @NotBlank
    @NonNull
    @FutureOrPresent(message = "The date cannot be in the past!")
    private LocalDateTime dueDate;
}
