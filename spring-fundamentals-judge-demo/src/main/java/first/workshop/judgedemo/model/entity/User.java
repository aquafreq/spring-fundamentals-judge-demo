package first.workshop.judgedemo.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Column
    @NotBlank
    @NonNull
    @Length(min = 2,message = "username length must be minimum two characters!")
    private String username;

    @Column
    @NotBlank
    @NonNull
    @Length(min = 2,message = "password length must be minimum two characters!")
    private String password;

    @Column
    @Email
    private String email;

    @Column
    @Pattern(regexp = "(?=(https:/github.com/)).*")
    private String git;

    @OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinColumn(name="role_id", referencedColumnName = "id")
    private Role role;
}
