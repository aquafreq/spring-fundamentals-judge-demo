package first.workshop.judgedemo.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserServiceModel {
    private String username;
    private String email;
    private String git;
}
