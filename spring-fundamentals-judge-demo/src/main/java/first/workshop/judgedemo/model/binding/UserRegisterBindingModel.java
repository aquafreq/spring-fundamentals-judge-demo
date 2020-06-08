package first.workshop.judgedemo.model.binding;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ScriptAssert(lang = "javascript",
        script = "_this.confirmPassword !== null &&_this.password === _this.confirmPassword",
        reportOn = "confirmPassword",
        message = "Passwords don't match lol")
public class UserRegisterBindingModel {

    @NotBlank
    @NonNull
    @Length(min = 2, message = "username length must be minimum two characters!")
    private String username;

    @NotBlank
    @NonNull
    @Length(min = 2, message = "password length must be minimum two characters!")
    private String password;

    private String confirmPassword;

    @NotBlank
    @NonNull
    @Email(message = "omfg wrong email, plz do better")
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=(https:/github.com/)).*", message = "wow wrong get git url, trea da ima https:/github.com/")
    private String git;
}
