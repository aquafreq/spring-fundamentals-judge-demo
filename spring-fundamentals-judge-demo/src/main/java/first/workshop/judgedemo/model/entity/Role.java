package first.workshop.judgedemo.model.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Role extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @NotNull
    @NonNull
    private RoleEnum name;
}
