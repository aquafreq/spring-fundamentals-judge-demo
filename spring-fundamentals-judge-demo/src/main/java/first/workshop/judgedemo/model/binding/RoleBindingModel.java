package first.workshop.judgedemo.model.binding;

import first.workshop.judgedemo.annotation.NotThatValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class RoleBindingModel {

    @NonNull
    @NotBlank
    @Pattern(regexp = "^(?!(Select user)).*$", message = "plz select user")
    private String username;

    @NonNull
    @NotBlank(message = "no role, no add")
    private String role;

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
