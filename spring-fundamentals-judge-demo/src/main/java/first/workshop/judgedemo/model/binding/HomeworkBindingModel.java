package first.workshop.judgedemo.model.binding;

import first.workshop.judgedemo.annotation.NotThatValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class HomeworkBindingModel {
    @NotThatValue(forbiddenValue = "Select exercise", message = "plz pick sum value")
    private String exercise;

    @Pattern(regexp = "^https:/github.com/.*?/.*?/*$", message = "valid git plz")
    private String gitAddress;
}
