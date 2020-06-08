package first.workshop.judgedemo.model.binding;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserLoginBindingModel {

    @NotBlank(message = "no blankerino")
    @NonNull
    @Length(min = 2, message = "Username must be more than 2 characters!")
    private String username;

    @NotBlank(message = "no blankerino")
    @NonNull
    @Length(min = 2, message = "Password must be more than 3 characters!")
    private String password;
}
