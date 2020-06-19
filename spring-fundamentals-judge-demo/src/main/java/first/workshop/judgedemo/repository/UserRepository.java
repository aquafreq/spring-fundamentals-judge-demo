package first.workshop.judgedemo.repository;

import first.workshop.judgedemo.model.entity.Role;
import first.workshop.judgedemo.model.entity.User;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> getByUsernameAndPassword(String username, String password);

    @Query("select u from User u join Role r on u.role.id = r.id where u.username = ?1 and u.role.id = ?2")
    Optional<User> getByUsernameAndRole(String username, String role);

    Optional<User> getByUsernameAndRole(String username, Role role);

    Optional<User> getByUsername(String username);

    User getById(String id);

    User getByGit(String git);
}
