package first.workshop.judgedemo.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Homework extends BaseEntity {
    private LocalDateTime addedOn = LocalDateTime.now();

    @Pattern(regexp = "(?=(https:/github.com/)).*")
    private String gitAddress;


    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

//    @OneToMany
//    @JoinColumn(name="exercise_id", referencedColumnName= "id")
//    private Set<Exercise> exercise;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Exercise exercise;
}
