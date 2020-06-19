package first.workshop.judgedemo.model.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceProfileInfoModel {
    private String username;
    private String email;
    private String git;
    private String sentHomework;
}
