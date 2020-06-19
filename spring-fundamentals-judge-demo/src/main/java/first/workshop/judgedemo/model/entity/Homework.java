package first.workshop.judgedemo.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Homework extends BaseEntity implements Comparator<Homework> {
    private LocalDateTime addedOn = LocalDateTime.now();

    @Pattern(regexp = "(?=(https:/github.com/)).*", message = "valid git plz")
    private String gitAddress;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    private Exercise exercise;

    public Homework(String gitAddress, User author, Exercise exercise) {
        this.gitAddress = gitAddress;
        this.author = author;
        this.exercise = exercise;
    }

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},
    mappedBy = "homework")
    private Set<Comment> comments;

    @Override
    public int compare(Homework o1, Homework o2) {
        return o2.getComments().size() - o1.getComments().size();
    }
}
