package first.workshop.judgedemo.model.service;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserServiceRegisterModel {
    @NotBlank
    @NonNull
    @Length(min = 2, message = "username length must be minimum two characters!")
    private String username;

    @NotBlank
    @NonNull
    @Length(min = 2, message = "password length must be minimum two characters!")
    private String password;

    @JsonIgnore
    private String confirmPassword;

    @NotBlank
    @NonNull
    @Email
    private String email;

    @Pattern(regexp = "(?=(https:/github.com/)).*")
    private String git;
}
