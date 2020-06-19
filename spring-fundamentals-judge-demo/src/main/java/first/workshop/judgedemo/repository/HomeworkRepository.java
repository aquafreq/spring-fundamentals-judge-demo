package first.workshop.judgedemo.repository;

import first.workshop.judgedemo.model.entity.Homework;
import first.workshop.judgedemo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework,String> {
    List<Homework> getByAuthor(User user);

    @Query("select h from Homework h where h.author.id  <> ?1 order by size(h.comments)")
    List<Homework> getHomeworkFromOtherUsers(String currentUserId);

    List<Homework> getByGitAddress(String gitAddress);
}
