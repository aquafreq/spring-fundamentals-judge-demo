package first.workshop.judgedemo.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserViewModel {
    private String id;
    private String username;
    private String roleName;
}
