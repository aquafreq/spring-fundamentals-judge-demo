package first.workshop.judgedemo.model.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserServiceLoginModel {
    private String id;
    private String username;
    private String password;
    private String roleName;
}
