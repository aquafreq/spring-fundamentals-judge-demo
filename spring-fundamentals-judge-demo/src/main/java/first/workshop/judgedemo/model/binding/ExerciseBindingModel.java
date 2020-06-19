package first.workshop.judgedemo.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExerciseBindingModel {

    @NotBlank
    @NonNull
    @Length(min = 3, message = "Exercise name length must be more than 2 characters")
    private String name;

    @NonNull
    @PastOrPresent(message = "The date cannot be in the future!")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startedOn;

    @NonNull
    @FutureOrPresent(message = "The date cannot be in the past!")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dueDate;
}
