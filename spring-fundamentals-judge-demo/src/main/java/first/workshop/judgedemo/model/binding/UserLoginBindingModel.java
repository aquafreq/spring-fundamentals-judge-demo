package first.workshop.judgedemo.model.binding;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserLoginBindingModel {

    @NotBlank(message = "no blankerino username")
    @NonNull
    @Length(min = 2, message = "Username must be more than 2 characters!")
    @Pattern(regexp = "([a-zA-z]+)", message = "plz let it be with letters only")
    private String username;

    @NotBlank(message = "no blankerino pass")
    @NonNull
    @Length(min = 2, message = "Password must be more than 2 characters!")
    private String password;
}
