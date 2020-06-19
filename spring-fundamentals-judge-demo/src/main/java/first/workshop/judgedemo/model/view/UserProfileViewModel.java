package first.workshop.judgedemo.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileViewModel {
    private String username;
    private String email;
    private String git;
    private String sentHomework;
}
